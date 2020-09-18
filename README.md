# ZeptTools
Simply put, this plugin was made to manage my own servers a bit better than what is out there. <br>
Runs a backup engine on a timer that will trigger at the interval specified in the config.

## Commands ##
Currently only has two commands
```
/zadmin <action = [stop]> <args[]>
/zbackup <action = [run, last]> <args[]>
```
## Config ##
Currently only has two commands
```yaml
maxSaveFiles: 10 # Maximum amount of backup files stored. (default = 10)

monitorInterval: 30 # Time in seconds to wait between Monitor Passes
backupInterval: 28800 # Time in seconds to wait between Backup Passes

chatMonitorHookUrl:  '<WebhookURL here>' # Webhook for chat monitor
tpsMonitorHookUrl :  '<WebhookURL here>' # Webhook for tps monitor
cpuMonitorHookUrl :  '<WebhookURL here>' # Webhook cpu chat monitor

lastSave: 1599100490815 # Keeps last updated time value on disk for reliability, DO NOT TOUCH
```
## Contributing ##
This is mostly a personal project to manage my own Minecraft servers. <br>
If you are willing to contribute, go ahead and send me a Pull Request. :)
