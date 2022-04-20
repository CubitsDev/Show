# Show Plugin
This plugin allows you to make amazing 'shows' in Minecraft with simple files alongside command blocks!  
<br/>

## Installation
Drag and Drop the .jar into your servers plugins folder. You'll also need the following plugins installed:

**Worldedit:** https://dev.bukkit.org/projects/worldedit

**OpenAudioMC:** https://www.spigotmc.org/resources/...-music-and-effects-bungeecord-velocity.30691/

**ProtocolLib:** https://www.spigotmc.org/resources/protocollib.1997/

Once installed, it'll auto generate folders.  
<br/>

## Tutorials
Every element of a show is an 'action', and your show file consists of many actions.

**Actions:** https://github.com/CubitsDev/Show/blob/master/actions.md

### Getting Started
To start, create a new folder in the plugins `shows` directory, and name it the same as the world the show will run in.  
By default this is called `world` on spigot servers.  
Within this create a file with your favorite editor such as VSCode or Notepad++, called `test.show`.  
You can now add actions to the file, but make sure to include the show definition.  

### Running A Show
To run your new show, simple use the command:  
```/show start test```

Congratulations, you just made your first super basic show!  
<br/>

## Showgen
Showgen allows for the quick and easy generation of 'FakeBlocks'.  
FakeBlocks are used to change an existing structure to something completely different without actually modifying the build.  
They use packets, to make the clients (players) think the blocks were edited, when in reality they werent.  
This can be useful when changing the color of walls, moving large structures, and many more things all while keeping your builds 100% safe!  

### Creating A GitHub Token
Showgen has a prerequisite of needing a GitHub token to post the show lines it generates to a 'GitHub Gist'.  
It's super easy to setup, follow this guide GitHub provides. [Guide](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)  
The only scope needed is called `gists`.  
I would recommend having a `service` account for this, as to not spam your own GitHub gists.  
Once you have obtained a token, open the `config.yml` file in the `Show` plugin folder and add your key to the appropriate line.  

### Using Showgen
Once added and your has server restarted, you can start generating show files!  

Start by selecting your default 'scene' with a WorldEdit wand.  
Your new scene has to be the same size so make sure you know how big the region is.  
Following this, run `/showgen setinitialscene`.  
This will save the state of your initial scene, so we can compare it with your new scenes.  

Next head to the `North West bottom` corner of your initial scene (Press F3).  
This is super important that it's the north west corner as this is the corner used to determine where to build your new scene.  
Once found, run `/showgen setcorner <x,y,z>`.  

Next, head to your new scene. Select it the same as you did the initial one, same size, same 1st position, etc (but different coordinates).  
Finally, do `/showgen generate FakeBlock 1`.  

If you have done everything correct, this should send you a link with your new lines you can add to your show file!  
Keep in mind, large selections may take some time to process.  
<br/>

# Commands
`/show - Main command - show.main (permission)`  
`/showdebug - Debug Command - show.debug (permission)`  
`/showgen - Generation Commands - show.showgen (permission)`  

# FAQ
**Q:** My show says it has started but does nothing!  
**A:** Run `/showdebug` to see any errors. It should give a reason and which line has caused it.  

**Q:** Is this the plugin palace used?  
**A:** Yes! This is a fork of the original code that people such as `Legobuilder0813`, `Innectic`, `Parker` and myself worked on. However this has been modified to work outside the 'Palace ecosystem'.  

**Q:** The audio isn't on time!  
**A:** This tends to be from users' internet connections and OpenAudioMC playing files. For the regions, it'll auto catch up, but single sounds may be out of time. Having a CDN would be the best but most expensive solution.  

# Missing/Future Stuff
Right now certain things such as `ShowBuild` had to be temporarily removed as they would require some reworking to get them in a nice way again.  
That's not to say they won't be added back in future though, and contributions are welcomed!  

Please leave any bugs/questions/features here and we will try out best to help you!  

Credit to `Legobuilder0813`, `Innectic`, `Parker` as well as me for working on the plugin over the years <3  
