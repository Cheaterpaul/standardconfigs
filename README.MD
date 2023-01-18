# Standard Configs Mod

This mod simply copies all files of from the `standardconfigs` folder to the `config` and `defaultconfig` folder. This is only done if the file does not exist.


## Why?

When updating a mod pack, all changes to the config files get overridden. This mod prevents this by just copying the files if they do not exist, keeping all local changes.

There are other mods that do something similar, but do not work for arbitrary files or have no working documentation.

## Usage
Place the config files in the following folders:

### `standardconfigs/client`
Target Folder: `config`  
for client only configs. Typically `-client.toml` files, shader configurations or similar.
### `standardconfigs/common`
Target Folder: `config`  
for client and server specific configs. Typically `-common.toml` files or files that are used by the client and the server.
### `standardconfigs/server`
Target Folder: `defaultconfig`  
for server only or world specific configs. Typically `-server.toml` files that are only used by the server or a world. 