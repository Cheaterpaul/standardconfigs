# Modpack config files

This mod copies config files of from the `modpackconfig` folder to the `config` and `defaultconfig` folder.

## Why?

Modpacks typically override changes to the config files made by the user.
This mod simply copies the config files to the config folders once.

## Usage
Place the config files in the following folders:

### `modpackconfig/client`
for client only configs. Typically `-client.toml` files.
### `modpackconfig/common`
for client and world specific configs. Typically `-common.toml` files.
### `modpackconfig/server`
for world specific configs. Typically `-server.toml` files.