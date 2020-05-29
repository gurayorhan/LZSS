# LZSS
LZSS is a dictionary coding technique. It attempts to replace a string of symbols with a reference to a dictionary location of the same string.

## Text<br/>
  0: I am Sam<br/> 
  9:<br/> 
 10: Sam I am<br/>
 19:<br/>
 20: That Sam-I-am!<br/>
 35: That Sam-I-am!<br/>
 
## Result<br/>
  0: I am Sam<br/>
  9:<br/>
 10: (5,3) (0,4)<br/>
 16:<br/>
 17: That(4,4)-I-am!(19,16)<br/>
