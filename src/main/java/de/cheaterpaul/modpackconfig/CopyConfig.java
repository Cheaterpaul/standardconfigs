package de.cheaterpaul.modpackconfig;

import com.google.common.io.Files;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CopyConfig {

    private static final String path = "modpackconfig";
    private static final Logger LOGGER = LogManager.getLogger("ModpackConfig");

    private static Path getPresetConfigPath() {
        return FMLPaths.GAMEDIR.get().resolve(path);
    }

    private static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    private static Path getDefaultConfigPath() {
        return FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
    }

    private static List<Pair<File, String>> getConfigFiles(ConfigType type) {
        File folder = new File(getPresetConfigPath().toFile(), type.getChildPath());
        if (!folder.exists()) return Collections.emptyList();
        try (Stream<Path> files = java.nio.file.Files.find(folder.toPath(), Integer.MAX_VALUE, ((path1, basicFileAttributes) -> basicFileAttributes.isRegularFile()))) {
            return files.map(Path::toFile).map(file -> Pair.of(file, trimStart(file.getAbsolutePath(), folder.getAbsolutePath()))).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(type.getMarker(),"Could not copy files", e);
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static boolean copyConfig(Pair<File, String> file, ConfigType type) throws IOException {
        File to = new File(type.getTargetFolder(), file.getValue());
        if (to.exists()) return false;
        Files.createParentDirs(to);
        Files.copy(file.getKey(), to);
        return true;
    }

    private static void copyConfig(ConfigType type) {
        LOGGER.info(type.getMarker(), "Copying config files");
        Collection<Pair<File, String>> configFiles = getConfigFiles(type);
        int copied = 0;
        int skipped = 0;
        List<Pair<File, Exception>> errors = new ArrayList<>();
        for (Pair<File, String> configFile : configFiles) {
            try {
                if (copyConfig(configFile, type)) {
                    copied++;
                    LOGGER.debug(type.getMarker(), "Copied config file {}", configFile.getKey().getName());
                } else {
                    skipped++;
                    LOGGER.debug(type.getMarker(),"Skipped config file {}", configFile.getKey().getName());
                }
            } catch (IOException e) {
                errors.add(Pair.of(configFile.getLeft(), e));
            }
        }
        LOGGER.info(type.getMarker(),"Finished copying: {} copied, {} skipped, {} errors", copied, skipped, errors.size());
        errors.forEach(pair -> {
            LOGGER.error(type.getMarker(),"Error copying config file {}", pair.getLeft(), pair.getRight());
        });
    }

    public static void copyClient() {
        copyConfig(ConfigType.CLIENT);
    }

    public static void copyCommon() {
        copyConfig(ConfigType.COMMON);
        copyConfig(ConfigType.SERVER);
    }

    public enum ConfigType {
        CLIENT("client", CopyConfig::getConfigPath),
        COMMON("common", CopyConfig::getConfigPath),
        SERVER("server", CopyConfig::getDefaultConfigPath);

        private final String childPath;
        private final Supplier<Path> targetFolderSupplier;
        private final Marker marker;

        ConfigType(String childPath, Supplier<Path> targetFolderSupplier) {
            this.childPath = childPath;
            this.targetFolderSupplier = targetFolderSupplier;
            this.marker = MarkerManager.getMarker(toString());
        }

        public String getChildPath() {
            return childPath;
        }

        public File getTargetFolder() {
            return targetFolderSupplier.get().toFile();
        }

        public Marker getMarker() {
            return marker;
        }
    }

    private static String trimStart(String source, String trim) {
        return source.replace(trim, "");
    }

}
