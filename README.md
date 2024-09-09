# CatusRTP

CatusRTP is a Minecraft plugin that allows players to teleport randomly within a world using the `/rtp` command. The plugin is designed to be easily configurable through code and provides a seamless integration for various server types. Whether you want it for practice servers, PvP, or survival, this plugin can be customized to fit your needs. If you prefer simplicity, this plugin is a great choice, and you can modify the code and build it yourself!

## Comparison Table

| Feature                | Supported |
|------------------------|-----------|
| **Overworld terrain**  | YES       |
| **Nether terrain**     | YES       |
| **The End terrain**    | NO        |
| **WorldBorder Safe Check** | NO    |
| **Permissions**        | YES       |
| **Config File (`config.yml`)** | NO    |
| **DeluxeMenus Support**| NO (unless command is replaced) |

## Commands

- `/rtp`: Teleports the player to a random location within their current world.
- `/rtp <world_name>`: Teleports the player to a random location within the specified world.

## Configuration

CatusRTP does not use a `config.yml` file. Configuration options are directly defined in the code. Key settings include:

- `RADIUS`: The radius for random teleportation (default: 1000)
- `DISABLE_WORLDS`: List of worlds where teleportation is disabled (e.g., ["world_nether", "world_the_end"])
- `TIME_COOLDOWN`: Cooldown time between uses of `/rtp` command (default: 10 seconds)
- `RTP_COOLDOWN`: Countdown time before teleportation occurs (default: 5 seconds)
- `ON_MOVE`: Enable/Disable player movement during countdown (default: true)
- `MESSAGES`: Customizable messages with color codes and RGB values
- `SOUNDS`: Customizable sounds for countdown, success, and cancellation
