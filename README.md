# 💤 SleepBetter &nbsp; [![Build Status](https://travis-ci.org/zekroTJA/SleepBetter.svg?branch=master)](https://travis-ci.org/zekroTJA/SleepBetter) [![GitHub release](https://img.shields.io/github/release/zekrotja/SleepBetter.svg)](https://github.com/zekroTJA/SleepBetter/releases)

A spigot plugin to go to sleep when a defined percentage of players are in bed.

## Download & Installation

You can downlaod the latest tagged artifacts from the [**Releases page**](https://github.com/zekroTJA/SleepBetter/releases).

Then, put the file in the `plugins` directory of your Spigot server and enter `/reload` in the Spigot console or (re)start the server.  
After that, a config file (`plugins/SleepBetter/config.yml`) will be generated where the plugin can be enabled/disabled and where the percentage of players needed to skip a night can be set.

*The config file looks like following:*
```yml
# Weather or not to enable the sleep-on-percentage function of this plugin
enable:  true

# How many players need to be in bed.
# Example:
#   0.5  -> 50% -> at least 5/10
#   0.75 -> 75% -> at least 8/10
#   and so on...
part: 0.5

# How many players needs to be on the server until
# SleepBetter enables.
requiredPlayerCount: 2
```

## Inspiration

This plugin was originally inspired by [Xferno2/Good-Night-Sleep-Tight](https://github.com/Xferno2/Good-Night-Sleep-Tight). I actually palned to fork his repo and do a pull-request with changes to be made, but at the end I thought it is easier to create an own version of it. This plugin may contain some of Xferno2's code structure because it is heavily inspired by their project.
