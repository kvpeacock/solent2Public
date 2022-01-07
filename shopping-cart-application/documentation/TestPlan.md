# Shopping Cart APPLICATION
### Test Plan
This test plan tests against all the requirements of the solution.
As such, if the application passes all the tests in the test plan, it will be considered fully operational and complete.

| Test Case 	| Action                                                                                            	| Expected Reaction                                                                                                                                                                                 	| Result | Notes |
|-----------	|---------------------------------------------------------------------------------------------------	|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|----------------------------------|-|
| TC1   	| User enters accesses any valid webpage within theshopping application. | If the server is running, the corresponding webpage is loaded. If the server is not running, a web page opens detailing a 404 error. |Passed	||   
| TC2.1     | User enters valid data and attempts to register an account. | An account is generated. User is returned to the user details webpage.	|Passed	| |         
| TC2.2     | User enters invalid data and attempts to register an account. | An account is  not generated, and the user is informed of the error. 	|Passed	|         |                       
| TC3.1     | User enters in valid data and attempts to login. | The user is logged in.	|Passed	|        |
| TC3.2     | User enters in invalid data and attempts to login. | The user is not logged in, and the user is informed of the error.	|Passed	| |
| TC4.1     | User requests to logout. | The user is logged out, and the session destroyed.	|Passed	|        |
| TC5       | User requests the app to perform any action. | The action is performed within 1 second.	|Passed	| | 
| TC6       | User submits a valid form. | The form is submitted.	|Passed	|    |
| TC7       | User submits an  invalid form. | The form is not submitted, and an error is shown.	|Passed	| |
| TC8.1     | User requests to add an in-stock item to their cart. | The item is added to their cart.	|Passed	|          |
| TC8.2     | User requests to add an out-of-stock item to their cart. | The item is not added to their cart. The user is informed that the item is out-of-stock.	|Passed	|  |                         
| TC9		| User requests to remove an item from their cart. | The item is removed from the cart. | Passed ||
| TC11.1    | User requests to purchase items that are in stock | They are forwarded to the transaction webpage| Failed |The bank service has not been implemented, so no transaction webpage exists.|
| TC11.1    | User requests to purchase items that are out of stock | The purchase request is rejected, with an error message. | Passed ||
| TC12      | User confirms a transaction | The transaction request is sent to the bank client. | Failed | The bank service has not been implemented, so there is no communication with the bank client.|
| TC13      | User confirms a transaction | The transaction is logged to a transaction log file | Failed |The bank service has not been implemented, so all 'transactions' are accepted by default. The log details a user's order number and amount due, instead of transaction replies from the banking client.|
| TC14		| A transaction is successful | An invoice is generated. Item stock levels are reduced. Invoice status set to PENDING | Passed |The bank service has not been implemented, so all 'transactions' are accepted by default.|
| TC15		| A transaction is rejected | An invoice is generated. Stock levels are not reduced. Invoice status set to REJECTED. | Failed |The bank service has not been implemented, so no 'transactions' can fail.|
| TC16		| User requests to view their invoices | The invoice webpage is loaded, displaying the user's past invoices | Passed ||
| TC17		| User requests to view their account details | The user details webpage is loaded, displaying the user's details. | Passed ||
| TC18		| User requests to update their account details | The user's details are updated. | Passed ||
| TC19	    | Admin configures the shopkeeper card properties file. | The credentials of the card need to be stored securely in a local properties file. | Failed | The banking service is not implemented, so the properties file has not been created. |
| TC20      | Admin starts up the application. | The properties file is read on start-up, automatically recognising the shopkeeper's card. | Failed | The banking service is not implemented, so the properties file has not been created. |
| TC21      | Admin tries to view a log of all transactions.| A persistant transaction log file containing transaction details can be read from local storage| Failed | A transaction log file exists, however the bank service has not been implemented, so all 'transactions' are accepted by default. The log details a user's order number and amount due, instead of transaction replies from the banking client.|
| TC22	    | Admin requests to view the catalog. |	A list of all catalog items is displayed.| Passed |
| TC23      | Admin requests to view a catalog item.	| The catalog item details are displayed. | Passed |
| TC24      | Admin requests to update a catalog item.	| The catalog item details are updated. | Passed |
| TC25      | Admin requests to delete a catalog item.	| The catalog item item is deleted. Any instances of the item stored in the cart are also deleted. | Passed |
| TC26      | Admin requests to view all users.	| A list of all users is displayed. | Passed |
| TC27      | Admin requests to view a user's details.	| The user details are displayed. | Passed |
| TC28      | Admin requests to update a user's details.	| The user details are updated. | Passed |
| TC29.1    | Admin requests to view all invoices.	| A list of all invoices are displayed. | Passed |
| TC29.2    | Admin requests to search for an invoice.	| A list of all invoices are displayed, which match the search query. | Passed |
| TC30      | Admin requests to view an invoice.	| The invoice details are displayed. | Passed |
| TC31      | Admin requests to edit an invoice status.	| The invoice status is updated. | Passed |
| TC29      | Non-admin attempts to access an admin only webpage.	| They are returned to the home page, with an 'Access Denied' message displayed. | Passed |