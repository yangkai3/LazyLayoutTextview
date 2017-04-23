# LazyLayoutTextview

try to cache Layout of the text to avoid invoke requestLayout while set text in TextView
try to not measure if text width not change

Alert!!! : 
1. text in the TextView must only contains number 0-9
2. now only handle the single line (multi line will support later)

see also:
http://instagram-engineering.tumblr.com/post/114508858967/improving-comment-rendering-on-android
