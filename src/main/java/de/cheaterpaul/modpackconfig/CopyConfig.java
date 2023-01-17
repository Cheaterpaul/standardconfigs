package de.cheaterpaul.modpackconfig;

import com.google.common.io.Files;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CopyConfig {

    private static final String path = "modpackconfig";
    private static final Logger LOGGER = LogManager.getLogger();

    private static Path getPresetConfigPath() {
        return FMLPaths.GAMEDIR.get().resolve(path);
    }

    private static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    private static Path getDefaultConfigPath() {
        return FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());
    }

    private static Collection<File> getConfigFiles(ConfigType type) {
        File folder = new File(getPresetConfigPath().toFile(), type.getChildPath());
        if (!folder.exists()) return Collections.emptyList();
        File[] files = folder.listFiles();
        return files != null ? List.of(files) : Collections.emptyList();
    }

    @SuppressWarnings("UnstableApiUsage")
    private static void copyConfig(File file, ConfigType type) throws IOException {
        File to = new File(type.getTargetFolder(), file.getName());
        if (to.exists())return;
        Files.copy(file, to);
    }

    private static void copyConfig(ConfigType type) {
        getConfigFiles(type).forEach(file -> {
            try {
                copyConfig(file, type);
            } catch (IOException e) {
                LOGGER.error("Could not copy '" + file.getPath() + "' config preset to "+ type.getTargetFolder().getPath(), e);
            }
        });
    }

    public static void copyClient() {
        copyConfig(ConfigType.CLIENT);
    }

    public static void copyCommon() {
        copyConfig(ConfigType.COMMON);
        copyConfig(ConfigType.SERVER_CONFIG);
    }

    public enum ConfigType {
        CLIENT("client", CopyConfig::getConfigPath),
        COMMON("common", CopyConfig::getConfigPath),
        SERVER_CONFIG("server", CopyConfig::getDefaultConfigPath);

        private final String childPath;
        private final Supplier<Path> targetFolderSupplier;

        ConfigType(String childPath, Supplier<Path> targetFolderSupplier) {
            this.childPath = childPath;
            this.targetFolderSupplier = targetFolderSupplier;
        }

        public String getChildPath() {
            return childPath;
        }

        public File getTargetFolder() {
            return targetFolderSupplier.get().toFile();
        }
    }

}
