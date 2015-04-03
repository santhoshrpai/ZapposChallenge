Please follow the link http://challenge.zappos.biz/c9dg/


Following in Turing's Footsteps
Introduction
Those pesky Nazis are at it again. In a continuing effort to win World War II, they have devised an improved way of sending secret messages to their forces in the field. Using steganography, they are hiding Enigma encrypted messages within images and sending them via email (yeah, who knew they had that back in WWII?!?!).

Your Mission
You have intercepted one of these emails, containing the image below, and must race against time to uncover (decrypt) their plot!

The source image
The mission takes the form of two phases:

Phase I
Start by downloading the above image to your machine. To ensure that the file is not corrupt, you can verify its MD5 (9e27659560350bfd6b42283332ef0f1c).

The first step is to extract the encrypted message from the image data. The image is a 24bit bitmap. The 24-bit pixel format stores 1 pixel value per 3 bytes. Each pixel value defines the blue, green and red, 8-bits per each sample. Data is hidden in the image by manipulating the LSB of each of those sample bytes. This results in the hiding of 3 bits per pixel - an essentially undetectable change to the image when viewed.

Keep in mind that all data is stored in Big-endian byte order.

To help you on your way, the pixel array can be found here in the data stream:

Offset	Size	Notes
54	151,164 bytes	Pixel array (image data)
Reading the data

Start by gathering all of the LSBs from the pixel array.

To ensure that you have collected the data correctly, and thus complete phase one, the following should be true of your collected bits:

Offset	Size	Notes
0	4 bytes	Length of hidden data, excluding this value (signed int)
4	(len-3) bytes	The message payload (all ascii characters)
4+(len-3)	3 bytes	"EOL", or the hex values 45,4F,4C
If the last 3 bytes of your recovered data equal "EOL", then you have successfully completed Phase I!

Phase II
Now it's time to decrypt the secret Nazi plans! Fortunately for us, the workings of the Enigma machine have been known for quite awhile - and this is a simplified version at that. All the parts and settings for the Enigma machine are within the hidden data - we just need to reassemble them and decrypt the message. The parts are as follows:

Offset	Size	Notes
0	94 bytes	Static Wheel (94 ascii characters)
94	94 bytes	Reflector (47 pairs of ascii characters)
188	94 bytes	Rotor I (94 ascii characters)
282	94 bytes	Rotor II (94 ascii characters)
376	94 bytes	Rotor III (94 ascii characters)
470	1 byte	Rotor I Notch (ascii character)
471	1 byte	Rotor II Notch (ascii character)
472	1 byte	Rotor I Starting Position (ascii character)
473	1 byte	Rotor II Starting Position (ascii character)
474	1 byte	Rotor III Starting Position (ascii character)
475	((len-475)-3) bytes	Encrypted message data
Basic Operation

The basic Enigma Machine worked as shown in the following diagram:

Enigma Machine
Our machine will not use a plugboard, but we will be using 94 characters instead of the original 26 uppercase.

Imagine the pieces of our machine (taken from the hidden data), lined up just as they are in the above image.

Let's take the following simplified example (only using 26 characters), with Rotor I being the right wheel and Rotor III being the left wheel:

Rotor I:	ABCDEFGHIJKLMNOPQRSTUVWXYZ	(Static Side)
 	MOYWDLZCFHVXQATPEKUJNGBRSI	(Scrambler Side)
Rotor II:	ABCDEFGHIJKLMNOPQRSTUVWXYZ	(Static Side)
 	VQZBUMWKRITEAHFPGSCNJYDLOX	(Scrambler Side)
Rotor III:	ABCDEFGHIJKLMNOPQRSTUVWXYZ	(Static Side)
 	PHNOXSAYIDCQVMEJRUTZGKBLWF	(Scrambler Side)
Reflector:	HQ RE WS LV GY MA FO DJ CX PU ZT IK BN	 
Using the above settings, let's see how the encrypted character 'R' would run through the machine:

Action	Rotor I	Rotor II	Rotor III	Reflector	Rotor III	Rotor II	Rotor I
Encrypt 'R'	R > K	K > T	T > Z	Z > T	T > S	S > R	R > X
Decrypt 'X'	X > R	R > S	S > T	T > Z	Z > T	T > K	K > R
As you can see, by its design, the Enigma Machine used the same mechanism to encrypt as well as to decrypt.

How Rotors Work

As you can see from the above example, each rotor is made up of a static side and a scrambler side. The static side of each rotor uses the same values from the "Static Wheel", the scrambler side is defined by the Rotor I, II and III values respectively. The static side of the rotor does not move, it stays static. However, the scrambler side "rotates" or "steps". In code, we would step this side by taking the last value and moving it to the head.

Example: Using the example Rotor I from above, if we rotated, or stepped it twice, it would result in the following configuration:

Rotor I:	ABCDEFGHIJKLMNOPQRSTUVWXYZ	(Static Side)
 	SIMOYWDLZCFHVXQATPEKUJNGBR	(Scrambler Side)
Initial Configuration

Before we can start decrypting, we must configure our machine. Using the "Starting Position" values for each rotor, you need to step that rotor until the "Starting Position" character is at the 0 index of the scrambler side.

Example: Using the example Rotor I from above, if we had a starting position of 'V', we would step the scrambler side until it looked like this:

Rotor I:	ABCDEFGHIJKLMNOPQRSTUVWXYZ	(Static Side)
 	VXQATPEKUJNGBRSIMOYWDLZCFH	(Scrambler Side)
Stepping the Rotors

There is one more wrinkle in the Enigma Machine. You will have noticed the 'Notch' values. Before each character in an encrypted message is processed, the rotors are stepped using the following logic:

Rotor I steps by one position.
If Rotor I was on its 'Notch' position (before being stepped), Rotor II also steps.
If Rotor II was on its 'Notch' position (before being stepped), Rotor III also steps.
Keep in mind that these steps only take place on the scrambler side, not the static side.

Checking Your Progress

To see if you're on the right track, here is a sample message to run through for debugging (using the actual rotor configurations from the image payload):

        Decoded Sample:

        Neither a borrower nor a lender be; For loan oft loses both itself and friend, and borrowing dulls the edge of husbandry.


        Encoded Sample:

        ; ?`#{L$vx,*'=@W]CXvw(^D3+q-&'nBe!?(|Rxr(7j-x3_n4'.=lhY>V/t=9[0gyZRz[awc_An5aIwW)>-'x^ult7IY_**Y^i#XT`{sGujX0ULW,Vvg#+U\g
    
HINT: If you start out correct and then turn to gibberish, you know you are not doing the stepping correctly.

DFTW (Decrypt for the win)
If you have written your Enigma Machine correctly, you should now be able to run the encrypted message through, character by character, and unravel the Nazi mystery message.

BIG IMPORTANT STEP!!! WHEN YOU HAVE THE MESSAGE, EMAIL IT TO cweiss@zappos.com YOUR SUBMISSION TIME IS WHEN MY MAIL SERVER RECORDS RECEIVING YOUR ENTRY. IF YOU DON'T EMAIL IT, IT DOESN'T COUNT!!! AND THERE IS A FIRST AND SECOND PLACE, SO PLEASE DONT SHARE YOUR ANSWER IF YOU ARE FIRST!
Glückliche Jagd!

Hints and Other Stuff

LSB Extraction
For the first part of the challenge (the image part) you are going to need a piece of code that can read a bunch of bytes, extract their LSB, and then put those LSB's together into bytes 8 bits at a time. Consider this string:

BABBABBBBAABBABABAABAABBBAABAABBBAABAAAABBABAABBBBABBBBBBABABAAABAABAAAABAAABBABBAABAABBBAABBABB
If you extract the LSBs from this string, you should get

010010000110010101101100011011000110111100101100001000000101011101101111011100100110110001100100
And if you convert that string back into bytes 8 at a time, you should get (in ASCII)

Hello, World
Wheel Setup
Once you've extracted the data and setup your Enigma machine you may want to know whether you got the wheel config right. Here are the first three characters of each item so you'll know you got that part correct. Don't try to run your decryption if your Enigma isn't in this configuration yet - it won't work!

Static Wheel : (SPACE)!"#
Reflector : nZ$
Rotor 1 : m{Z
Rotor 2 : rEe
Rotor 3 : U|4
