# Actions

This is a list of all the actions you can put into a show file and how to use them.

```
Show	Location	0,10,0
Show	TextRadius	150
#This is the location where show/text is centered.
Show	Name    <nameofshow>

#Comment


### Text ###
#Text:
<seconds>	Text	<text>
3	Text	&eText

#Title:
#fadeIn, fadeOut, stay work in ticks (20 = 1 second)
<seconds>	Title	<Type>(title/subtitle)	fadeIn	fadeOut	stay	<text>
0	Title	title	10	10	40	&cTitle
4	Title	subtitle	10	10	40	Subtitle

#Colored Text: 
&0	BLACK
&1	DARK BLUE
&2	DARK GREEN
&3	DARK AQUA
&4	DARK RED
&5	DARK PURPLE 
&6	GOLD
&7	GRAY
&8	DARK GRAY
&9	INDIGO
&A 	GREEN
&B	AQUA
&C	RED
&D	PINK
&E	YELLOW
&F	WHITE
&M	STRIKE THROUGH
&N	UNDERLINED
&L	BOLD
&K	RANDOM
&O	ITALIC


### Blocks ###
#http://minecraft-ids.grahamedgecombe.com/
#Placing Blocks:

### 1.12 :
<seconds>       Block        id:data      x,y,z
3	Block	152	0,11,0

### 1.13 :
<seconds>       Block        block_string      x,y,z
3	Block	redstone_block	0,11,0

#Pulse: (Sets block to redstone block then back)
<seconds>       Pulse	x,y,z
5	Block	0,12,0

#Schematics: x,y,z = copy point Leave a few seconds b/w each schematic
<seconds>	Schematic	<name>	x	y	z	 <world>	<paste -a (air), (true/false)>
//schem save mce <name>
0	Schematic	example	0	12	0	dhs	true

#Fake Blocks: (Tells player a block exists, block doesn't actually (packet magic))

## 1.12 :
<seconds>       FakeBlock        id:data      x,y,z
3	FakeBlock	152	0,11,0

## 1.13 :
<seconds>       FakeBlock        block_string      x,y,z
3	FakeBlock	redstone_block	0,11,0


### Extra Effects ###
#Lightning:
<seconds>	Lightning	x,y,z
5	Lightning	0,10,0

#Particle
#types = https://docs.google.com/spreadsheets/d/1bqOeC0kg2VRLa5oGHhfYkyoyWwz7Mlt_i0LHrMZ6dvg/edit?usp=sharing
<seconds>	Particle	type	x,y,z	xoffset	yoffset	zoffset	speed	amt
6	Particle	cloud	0,10.2,0	2	0.2	4	0	20

#Glow with the show
Colors: Red, Orange, Yellow, Green, Aqua, Blue, Purple, Pink, White, Black (or r,g,b)
<seconds>	Glow	color	x,y,z	radius #x,y,z = center radius from center
0	Glow	255,255,255	0,10,0	15
10	GlowDone

#Fountains: 

## 1.12
<seconds>	Fountain	id:data	#	x,y,z	<directional force(x,y,z)>
# = time fountain is on
2.1	Fountain	35:0	1.9	0,10,0	0,0.8,0

## 1.13
<seconds>	Fountain	block_string	#	x,y,z	<directional force(x,y,z)>
# = time fountain is on
2.1	Fountain	white_wool	1.9	0,10,0	0,0.8,0

### Commands ###
#Run commands:
<seconds> Command   <command goes here (supports spaces & no slash)>

### Repeating Actions ###
#Repeats actions multiple times:
<seconds>   Repeat  <occurences>    <delay> <Normal Show Action (minus the starting seconds>

### Audio ###
<seconds>	AudioRegion <wgregionname>  <length>    <volume>    <linktosound>
0   AudioRegion tomtest 27  70  https://audio-files.palace.network/ogg/Laughinplace.ogg
<seconds>	AudioOnce	<volume>	<fileurl>
0 AudioOnce 70  https://audio-files.palace.network/ogg/restroom/toiletflush.ogg


### Fireworks ###
#Effects:
Effect	<name>	Type,Color&Color&Color,Color&Color,(optional)Flicker,(optional)Trail
Types		BALL, BALL_LARGE, BURST, STAR, CREEPER
Colors		Aqua (light blue), Black, Blue, Fuchsia (Pink), Gray, Green, Lime, Maroon (Magenta)
		Navy, Olive (Brown), Orange, Purple, Red, Silver, Teal (Cyan), White, Yellow (or r;g;b)
Effect	b1	BURST,255;255;255&WHITE,BLUE,FLICKER

#Firework:
<seconds>	Firework	<Location(x,y,z)>	<Effect,Effect...>	<time>	<Directional force,(x,y,z)>	1
0	Firework	0,10,0	b1	1	0,1,0	1

#Power Firework: This actually moves
<seconds>	PowerFirework	<Location(x,y,z)>	<Effect,Effect...>	<Directional force,(x,y,z)>
0	PowerFirework	0,15,0	b1	0,1,0


### Armor Stands ###
#ArmorStand, like Effect, but for Armor Stands
ArmorStand	<name>	<small(true/false)>	<head_id;chest_id;legs_id;boots_id;hand>
#For skulls on head: skull:playerTextureResourceHash
#For colored armor: armorID:data(r,g,b) <-no spaces

#Spawn:
<seconds>	ArmorStand	<name>	Spawn	x,y,z,rotation

#Move:	<time> to move from current position to x,y,z
<seconds>	ArmorStand	<name>	Move	x,y,z	<time>

#Position: change limb/body positions
#PositionTypes: HEAD, BODY, ARM_LEFT, ARM_RIGHT, LEG_LEFT, LEG_RIGHT
#<time> must be at least 0.1
<seconds>	ArmorStand	<name>	Position	<PositionType>	<anglex,angley,anglez>	<time>

#Rotate:
<seconds>	ArmorStand	<name>	Rotate	<+/-degree from current yaw>	<speed>
0	ArmorStand	Test	Rotate	50	1
0	ArmorStand	Test	Rotate	-50	1

#Despawn:
<seconds>	ArmorStand	<name>	Despawn

#Music:
<seconds>	Music	<disc id #>
```
