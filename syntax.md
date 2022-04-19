# Show Plugin Syntax & Actions
All the available actions will be explained in detail below.  
All action lines begin with `<seconds>`, which is how many seconds from the time you start the show before that line is run.  
We will omit this argument from the rest of the doc, but it is **mandatory** for all arguments.  
<br/>

## Show Definitions
All show files **must** begin with the below header.  
These lines **do not** start with `<seconds>`.  
```
Show	Location	<x,y,z>
Show	TextRadius	<radius>
Show	Name    <name>
```
`x,y,z` The center of the show.  
`Radius` How far away from the shows center will players receive messages from the show.  
`Name` The name of the show.  
<br/>

## Text Actions

### Text
This sends a simple message to all players in the shows `TextRadius`.  
```
Text    <text>
```
`Text` The text to send, with minecraft formatting codes supported. [Formatting Codes Cheatsheet](https://htmlcolorcodes.com/minecraft-color-codes/)  
<br/>

### Title & Subtitle
Show a title or subtitle in the middle of players screens.  
```
Title   <type>  <in>    <out>   <stay>  <text>
```
`Type` Type of title: `title` or `subtitle`.  
`In` Fade in speed.  
`Out` Fade out speed.  
`Stay` How long to show.  
`Text` The text to show, with minecraft formatting codes supported. [Formatting Codes Cheatsheet](https://htmlcolorcodes.com/minecraft-color-codes/)  
<br/>

## Block Actions

### Real Blocks
Places real blocks, just as if a player did.
```
Block   <blockName> <x,y,z>
```
`BlockName` Human-readable name of the block.  [Block Names & Ids](http://minecraft-ids.grahamedgecombe.com/)  
`x,y,z` Coordinates to place the block at.  

<details>
  <summary>Click To View 1.12 Syntax</summary>

```
Block   <id:data>   <x,y,z>
```
`id:data` Numerical id of the block.  [Block Names & Ids](http://minecraft-ids.grahamedgecombe.com/)  
`x,y,z` Coordinates to place the block at.
</details>
<br/>

### Fake Blocks
Uses packets to make the player think a block was placed, when really its not.  
```
FakeBlock   <blockName> <x,y,z> <data>
```
`BlockName` Human-readable name of the block. [Block Names & Ids](http://minecraft-ids.grahamedgecombe.com/)  
`x,y,z` Coordinates to place the block at.  
`data` Extra block data (Optional) [Block Data Guide](http://UNKNOWN/)  

<details>
  <summary>Click To View 1.12 Syntax</summary>

```
FakeBlock   <id:data>   <x,y,z>
```
`id:data` Numerical id of the block. [Block Names & Ids](http://minecraft-ids.grahamedgecombe.com/)  
`x,y,z` Coordinates to place the block at.  
</details>
<br/>

### Pulse
Quickly places and removes a Redstone block, used as a button to start command-block chains and anything else.  
```
Pulse   <x,y,z>
```
`x,y,z` Coordinates to place the block at.
<br/>

## Other Effects

### Lightning
Summons and lightning bolt.  
```
Lightning   <x,y,z>
```
`x,y,z` Coordinates where the lightning will hit.  
<br/>

### Particles
Summons and lightning bolt.
```
Particle   <type>   <x,y,z> <xOffset>   <yOffset>   <zOffset>   <speed> <amount>
```
`type` Human-readable name of the particle. [Supported Particle Names](https://docs.google.com/spreadsheets/d/1bqOeC0kg2VRLa5oGHhfYkyoyWwz7Mlt_i0LHrMZ6dvg/edit?usp=sharing)  
`x,y,z` Coordinates where the lightning will hit.  
`offset(s)` Random offset from coords, so not all particles spawn in same exact location. 
`speed` How fast the particles move. (Applies to some particles only)  
`amount` How many particles to spawn. (Each at random location within offset)  
<br/>

### Glow with the Show
Gives all players within the radius a colored helmet.
```
Glow   <color>  <x,y,z> <radius>
```
`color` Color to glow: `Red`, `Orange`, `Yellow`, `Green`, `Aqua`, `Blue`, `Purple`, `Pink`, `White`, `Black`, `(r,g,b)`.  
`x,y,z` Center coordinates.  
`radius` Radius to apply.  
<br/>

### Fountains
Spews flying block entities in the direction specified.  
```
Fountain   <blockName> <time>   <x,y,z> <directional-force-vector>
```
`BlockName` Human-readable name of the block. [Block Names & Ids](http://minecraft-ids.grahamedgecombe.com/)  
`time` How long to run for.  
`x,y,z` Coordinates to shoot from.  
`directional-force-vector` Force applied to each block, in the format of a vector.  [Vectors](https://UNKNOWN/)  

<details>
  <summary>Click To View 1.12 Syntax</summary>

```
Fountain   <id:data> <time>   <x,y,z> <directional-force-vector>
```
`id:data` Numerical id of the block. [Block Names & Ids](http://minecraft-ids.grahamedgecombe.com/)
`time` How long to run for.  
`x,y,z` Coordinates to shoot from.  
`directional-force-vector` Force applied to each block, in the format of a vector.  [Vectors](https://UNKNOWN/)  
</details>
<br/>

### Commands
Executes a command as console.  
```
Command   <command>
```
`command` The command, supports spaces, but no leading `/`.  
<br/>

### Repeating Actions
Runs any show action many times.  
```
Repeat  <occurences>    <delay> <action>
```
`occurences` How many times to run the action.  
`delay` Time between each run. (Integer, no decimals)  
`action` Any other show actions, minus the leading `<seconds>`.  
<br/>

### Audio Once
Plays the audio (Using OpenAudioMC) once. [OpenAudioMC](https://help.openaudiomc.net/)  
```
AudioOnce   <volume>  <url>
```
`volume` How loud to play the audio.  
`url` URL where the audio is located.
<br/>

### Audio Region
Starts playing the audio (Using OpenAudioMC) for everyone in the region. [OpenAudioMC](https://help.openaudiomc.net/)
```
AudioRegion <region>    <length>    <volume>    <url>
```
`region` Name of the Worldguard region. [Worldguard](https://dev.bukkit.org/projects/worldguard)  
`length` How long to play the audio for (Seconds).  
`volume` How loud to play the audio.  
`url` URL where the audio is located.  
<br/>

### Music Disk
Plays a vanilla music disk.  
```
Music   <diskId>
```
`diskId` Numerical ID of the disk to play.  [Disks](https://minecraftitemids.com/types/music-disc)  
<br/>

## Fireworks

### Effect Definition
All fireworks must be defined before being used.  
```
Effect	<name>	<shape>,<colors>,<fadeColors>,<flicker>,<trail>
```
`name` Unique name for the firework.  
`shape` Shape of the firework: `BALL`, `BALL_LARGE`, `BURST`, `STAR`, `CREEPER`.  
`colors` Colors in the firework. (As many as you want, seperated by `&`)  
`fadeColors` Colors during fading. (As many as you want, seperated by `&`)
`flicker` Include `FLICKER` if you want it to flicker.  
`trail` Include `TRAIL` if you want it to have a trail.  

**Supported Colors:**  
`Aqua` (Light Blue), `Black`, `Blue`, `Fuchsia` (Pink), `Gray`, `Green`, `Lime`, `Maroon` (Magenta)
`Navy`, `Olive` (Brown), `Orange`, `Purple`, `Red`, `Silver`, `Teal` (Cyan), `White`, `Yellow`, `(r;g;b)`.  
<br/>

### Vanilla Firework
Shoots a firework.
```
Firework    <x,y,z> <effects>   <time>  <directional-force-vector>	1
```
`x,y,z` Coordinates to shoot from.  
`effects` Previously defined effects, comma separated. (No Spaces)  
`time` How long to run for.  
`directional-force-vector` Force applied to the firework, in the format of a vector.  [Vectors](https://UNKNOWN/)  
<br/>

### Power Firework
Shoots a firework rocket that moves.
```
PowerFirework   <x,y,z> <effects>   <directional-force-vector>
```
`x,y,z` Coordinates to shoot from.  
`effects` Previously defined effects, comma separated. (No Spaces)  
`directional-force-vector` Force applied to the firework, in the format of a vector.  [Vectors](https://UNKNOWN/)  
<br/>

## Armorstands

### Armorstand Definition
All armorstands must be defined before being used.
```
ArmorStand	<name>	<small>	<head_id>;<chest_id>;<legs_id>;<boots_id>;<hand_id>
```
`name` Unique name for the new armorstand.  
`small` Is the armorstand small: `TRUE`, `FALSE`.  
`head_id` Numeric ID of item or `skull:<playerTextureResourceHash>` for player head.  
`chest_id` Numeric ID of item or `<id>:data(<r,g,b>)` for dyed leather armor.  
`legs_id` Numeric ID of item or `<id>:data(<r,g,b>)` for dyed leather armor.  
`boots_id` Numeric ID of item or `<id>:data(<r,g,b>)` for dyed leather armor.  
`hand_id` Numeric ID of item or `<id>:data(<r,g,b>)` for dyed leather armor.
<br/>

### Spawn
Spawns an armorstand.
```
Armorstand   <name> Spawn   <x,y,z,rotation>
```
`name` Unique name of armorstand.  
`x,y,z,rotation` Coordinates and rotation to spawn at.  
<br/>

### Move
Moves an armorstand from its current position to the destination.
```
Armorstand   <name> Move    <x,y,z>  <time>
```
`name` Unique name of the armorstand.  
`x,y,z` Destination location.  
`time` How long does it take to move.  
<br/>

### Position
Positions and limbs/parts of an armorstand.
```
Armorstand   <name> Position    <part>  <xAngle,yAngle,zAngle>  <time>
```
`name` Unique name of the armorstand.  
`part` What part is moving: `HEAD`, `BODY`, `ARM_LEFT`, `ARM_RIGHT`, `LEG_LEFT`, `LEG_RIGHT`
`angle(s)` Rotation on each axis of the part.  
`time` How long does it take to move.  
<br/>

### Rotate
Rotates the entire armorstand.  
```
Armorstand   <name> Rotate  <change>  <time>
```
`name` Unique name of the armorstand.  
`change` + or - degree from current yaw.  
`time` How long does it take to move.  
<br/>

### Despawn
Despawns an armorstand.
```
Armorstand   <name> Despawn
```
`name` Unique name of the armorstand.  
<br/>