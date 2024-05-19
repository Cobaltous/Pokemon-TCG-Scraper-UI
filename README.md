# Pokemon-TCG-Scraper-UI
Pulls Pokemon and Pokemon Card data from PokemonDB and Pikawiz, respectively, to be arranged for viewing convenience.

This program scrapes and compiles TCG data to help pick out the best packs to buy for the purposes of either pulling cards of your favorites or yielding the best value. Currently, it only allows for finding your provided favorite Pokemon in sets and displaying sets by average card value (as determined by whatever pricing data Pikawiz/TCGPlayer has available) and the methods of doing so are quite primative and likely not very informative.

To input your favorite Pokemon for the program to search for, you can either enter each one's name (in order from most-to-least favorite), or you can optionally use the output from the Favorite Pokemon Picker from the Cave of Dragonflies website (https://www.dragonflycave.com/favorite.html).
You can just take the exported state from that once you've got a couple identified and paste the whole thing into the correct field - it should be able to parse the JSON output it yields.

Otherwise, it also has a function to list out each card per set that a selected Pokemon appears in if you just want the data on one - you can use it to ogle at the absurd Charizard card prices, for example.

UI is probably pretty buggy right now and has some things I'm looking to fix/add.

Additionally, for the sake of running faster, it will save backup files of each HTML page that it pulls until they become stale (1 week for Pokedex data, 1 day for card data) so you don't have to pull a fresh page every time you use the program, although you can if you'd like. You can pick out which folder it'll output to; otherwise, it'll create on in the same directory from where you run the program.


The program does have a dependency on jsoup and JSON-java to do its requesting and processing and whatnot, if you'd rather build from the provided source code than use the provided .jar file. It is, however, uncommentated and full of dummied text, so sift through that at your own risk. This is really only here to host it for me and one/two others right now.

PokemonDB Page: https://pokemondb.net/pokedex/stats/height-weight

Pikawiz Card Set Page: https://pikawiz.com/cards

jsoup Homepage: https://jsoup.org/

JSON-java repository: https://github.com/stleary/JSON-java
