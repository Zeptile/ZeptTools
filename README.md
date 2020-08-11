# ZeptTools
Simply put, this plugin was made to manage my own servers a bit better than what is out there. <br>
Runs a backup engine on a timer that will trigger at the interval specified in the config.

## Commands ##
Currently only has two commands
```
/zadmin <action = [stop]> <args[]>
/zbackup <action = [backup]> <args[]>
```
## Config ##
Currently only has two commands
```yaml
timerInterval: 8 # Time in hour to wait between Backup Passes
maxSaveFiles: 10 # Maximum amount of backup files stored. (default = 10)

lastSave: 1597167317730 # Keeps last updated time value on disk for reliability, DO NOT TOUCH
```
## Contributing ##
This is mostly a personal project to manage my own Minecraft servers. <br>
If you are willing to contribute, go ahead and send me a Pull Request. :)
