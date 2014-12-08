To Do:
 - Monetize: Allow user to upgrade to premium membership - this will open up 10 more free games
 - Sing the Alphabet: displays all letters (one graphic) and sings the ABC's in the background
 - What's The Time: make a dynamic clock where user can drag hand around and the correct digital time will update below
 - Sing Along: find some free to use nursery songs & add the lyrics
 - Short Sentences game
 - Make a Previous Button
 - Make a Home Button
 - Move Buttons to bottom of screen
 - Make a random button on the letter/number game
 - Make a Temperature game with thermometer
 - Make an animations game with some of the animations from RapBeats
 - Make a 3-d shape game that a user can spin the shape around
 - Make a "Drum Kit" game where user can press on a drum to make a noise
 - Make a "Sounds" game where user can press on a image, such as a cat, to make a noise
 - Make a "Memory" game where user will see 10 backs of cards: user clicks one sees image and tries to match it, 
 	if fail, flip back over, if match, then they are facing forward



 - Make a "Find the Letter" game where 4x4 table contains 16 images where one of them is the correct letter.
 - Make a "Find the Number" game where 4x4 table contains 16 images where one of them is the correct letter.
 - Make a "Find the Shape" game where 4x4 table contains 16 images where one of them is the correct letter.



 - Match the Symbol's: 3 on right to 3 on left, matching one side to the other with colors or a line
 
 - Need to update to API 2.2 for Drag 'n' Drop. Test phone with following and only display drag n drop game on appropriate device:

(http://stackoverflow.com/questions/3423754/retrieving-android-api-version-programmatically)

int currentapiVersion = android.os.Build.VERSION.SDK_INT;
if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO){
    // Do something for froyo and above versions
} else{
    // do something for phones running an SDK before froyo
}
http://developer.android.com/reference/android/os/Build.VERSION.html#SDK_INT
http://developer.android.com/reference/android/os/Build.VERSION_CODES.html 

 - Find a puzzle game w/ 4 pieces to the puzzle, many different puzzles: 4x4 grid. each piece is a square w/ no padding
 	- when all 4 pieces have the correct neighbors, congratulate user and display menu to do another puzzle or go Home
 	- drag 'n' drop will be tricky, but need to figure out how to put the piece in an "empty" space on drop & the correct place...
 - Drag 'n' Drop images: make a board where user can move about 5 or 6 objects around as they please