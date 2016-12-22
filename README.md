PaymentCardGenerator v1.0
=========================

See LICENSE for this software's licensing terms.

PaymentCardGenerator is a Java library that aids in testing payment card processing systems.
It generates random payment card numbers so that you don't have to use an actual card.

Card numbers are generated based on the criteria defined here:

* https://en.wikipedia.org/wiki/Luhn_algorithm
* https://en.wikipedia.org/wiki/Payment_card_number (as of December, 2016)


## Features

* Supports generation of American Express, VISA, MasterCard and Discover
* Easily extensible to support any type of payment card which uses Luhn validation.  All you need to do is add a member to the CardType enumeration.
* Numerous criteria for generating numbers, including by type, quantity, length and prefix
* Future-proof:  generate numbers based on your own criteria, even if the library doesn't have knowledge of the latest card number formats


## Donations

https://paypal.me/KurtisLoVerde/5

Thank you for your support!
