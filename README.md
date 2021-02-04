# Market Designer

## What does "Market Simulator" do?
Market Simulator is an application that allows a user to design their own 
market place, filled with vendors. Each vendor has an inventory that stores 
items that the vendor can sell to other vendors. These inventories have max 
carry weight, once this is met by the combined item weight inside, no more 
items may be added. Each vendor has a certain amount of money with which they 
can buy items. Each vendor has a certain amount of bartering power. If one 
vendor's bartering power is higher than another's, they can lower the value 
of one item in the other's inventory (this will decrease the bartering power 
of the vendor that used this ability, while raising the other's bartering 
power). At the start of a game, vendors can be created and the items that 
their inventories will start with will be determined by the user. Created 
vendors can then trade and barter with one another.

## Who will use "Market Simulator"?
Market Simulator is primarily intended to act as a game in which the player 
can customize their own market. It is for players who wish to simulate the 
exchange of goods between multiple vendors, and track the prosperity, or 
lack their of, over their series of trade deals.

## Why this project is of interest to me
I greatly enjoy simulation games (Sims 3), and wish to be able to create 
a custom, virtual market place to simulate trade dealings between vendors. 
I want to be able to observe what trade strategies work best, and which 
are less than ideal.

## User Stories
- As a user, I want to be able to add multiple new vendor to the market.
- As a user, I want to be able to get a vendor to barter with another vendor to reduce
  the price of an item before purchase.
- As a user, I want to be able to get a vendor to buy an item from another,
  vendor in exchange for money.
- As a user, I want to be able to select a vendor and list all the items in that 
  vendor's inventory.
- As a user, I want to be able to save a market's list of vendors, the data of each
  vendor, the inventory of each vendor, the list of items of each inventory, and the
  data of each item to a file (essentially I want to be able to save the entire market
  to a file).
- As a user, I want to be able to load a market's list of vendors, the data of each
  vendor, the inventory of each vendor, the list of items of each inventory, and the
  data of each item from a file (essentially I want to be able to load the entire 
  market from a file).
  
## Phase 4: Task 2
I created a button type hierarchy. Every button that is part of the GUI from phase 3
inherits (sometimes with a generation of separation) from the Button interface. Both
the interface MarketUIButton and ConfirmConscriptionButton inherit directly from Button. 
The MarketUIButton interface has a MarketGUI field, unlike button. ConfirmConscriptionButton 
overrides its addListener method where it adds an ActionListener that calls the 
ConscriptionGUI's method doConfirmConscription when the button is pressed. Likewise, all the
buttons on the MarketGUI implement the MarketUIButton interface, each overriding the 
addListener method. Each of those buttons that implements MarketUIButton will override 
addListener to add an ActionListener that calls a different method in MarketGUI (the "do" 
methods) for each implementation. Essentially, every button performs a different MarketGUI 
action when pressed.

## Phase 4: Task 3
- First of all, I would make the methods of the project robust.
  - Particularly the conscriptVendor method in the Market class and the add method in the 
    Inventory class. Instead of having boolean or Vendor return types, these classes would 
    throw appropriate exceptions on failure to perform their function.
  - Most classes with requires clauses would have that clause removed and replaced with 
    the ability to throw appropriate exceptions
  - All the aforementioned exceptions would either be handled in the code that calls them
  - Each robust method would have appropriate tests
- To reduce semantic coupling between the MarketCommandLine and MarketGUI classes (if they
  were to be used in conjunction) I would have both inherit from an interface, or use a 
  database that contains all the strings that both classes displayed.
- I would make the Audio system found in the MarketGUI into its own class, complete with
  tests
- I would separate the graphics and market manipulation functionality of the MarketGUI
  and ConscriptionGUI into 2 classes each
- I would make each button an observable and have the class that manipulates the market be
  an observer, and setup an appropriate observable-observer relationship
   - Since each button makes the market manipulator perform a different action, each button
     would most likely have to call a different "update" function in the Observer class 
     (would probably have to use own defined Observer)