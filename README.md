# VorePlugin
VorePlugin for minecraft 1.19.2

This plugin aims to add vore to the world of Minecraft in a vanilla-friendly way. If you do not know what vore is or dislike it leave now.
Currently it is pretty bare bones and buggy but I am working hard on fixing everything with my limited skills. :)

If you want more detailed information or wish to talk with me you can join me on [Discord](https://discord.gg/2hyrR5nzTA)!

## Features

- Vore (obviously)
- Full belly customization including swallow, digest and release messages
- Vore types and preferences
- PlaceholderAPI integration

## Planned Features

- A single GUI to replace the 15.000.000 commands
- Integration with the Customizeable Player Models API
- Object vore
- Post-digestion states (reformation, sentient fat, permavore)

# A Short Introduction

This plugin, as the name implies, revolves all around vore. To accomplish this there are a few (thousand) commands and a lot of buggy code.
There are several safety systems in place, so you can't eat people that don't want to get eaten and a loss of connection won't fuck you over.
Hopefully. Maybe. Idk, my code is a mess.

## Commands

- /setrank <rank>: Sets a palyers vore rank (Predator, Prey, Switch, Unset). Players without a rank (Unset rank) will be excluded from vore activities.
- /setbelly <name> <type>: Sets a belly with the given name and vore type to your current location.
- /preference <add|remove> <type>: Add or remove vore types from a blacklist. You cannot participate in vore types you blacklisted.
- /digest <name|all>: Digests the given player, if they are in one of your bellies. "all" will digest all players in any of your bellies.
- /release <name|all>: Releases the given player, if they are in one of your bellies. "all" will release all players in any of your bellies. (Yes, this line is copy pasted. Sue me.)
- /belly <name> <attribute> <value>: Edit a belly's attributes such as name, ambient effect, messages, acid strength and more.
- /vorestats <name>: View vore related stats about people! These include times eaten/digested and prey eaten/digested.
- /voretop <category>: Leaderboard of vore stats so you can flex your 200 prey eaten (or 200 times eaten for our prey friends).
- /vore <command> <arguments>: Soon to be the command to open the universal GUI, currently a collection of every other command (except the super secret testing command but shhhhh!).

## Quick Setup Guide (Server)

Just pop the plugin into the plugins folder and restart the server.
Everything should work now. If it doesn't, come yell at me on Discord by either making a forum post or spam pinging @Server Slut.

## Quick Setup Guide (Client)

When joining a server that has the plugin enabled, you will be greeted with a message telling you to set your vore rank.
By default, every player has the Unset rank, disallowing them from participating in vore.
Depending on what rank you choose, certain actions will be allowed/blocked.
Prey cannot eat people and Predators can't be eaten. Switches can both eat and be eaten.

Once you set your rank you're already good to go! You can go around and get eaten.

If you'd like to do the eating yourself, you'd want to set a belly.
Build yourself a nice stomach, womb or wherever else you want to have your prey and use the /setbelly command to set the belly.
Your belly is ready, and you can now punch someone to attempt to vore them.
Do note that your prey needs to be in survival or adventure mode for vore to work.

# Help Me, Plugin No Work!

If you need help of any kind, I'm almost always available on Discord.
Make a forum post or ping @Server Slut to get my attention and I'll be all yours.
For more serious bug reports or feature requests I don't discourage using the GitHub Issue Tracker.
I can't guarantee that I'll look at it often though as I'm quite lazy and switching tabs from YouTube to GitHub is hawd. qwq
