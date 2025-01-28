# Basic Cryptocurrency Exchange System
Objective: Write a program that simulates a simple cryptocurrency exchange using Java or Kotlin (Spring Boot is preferred).
The system should provide a RESTful API, without the need for a graphical user interface.

# Core Features:
**User Registration and Authentication**: Allow users to register and authenticate. Each user should have a unique username and password * use JWT. User data can be stored anywhere from file, to a simple DB.

**Wallet Management**: Enable users to create a wallet for storing different types of FIAT and cryptocurrencies. A wallet should contain multiple types of currencies (crypto: BTC, ETH, USDT, fiat: USD, EUR) and show balance. 

**Deposit and Withdrawal**: Implement a mock functionality to deposit or withdraw specific amounts of a cryptocurrency to and from a user's wallet via API.

**Trading**: Allow users to place buy, sell, exchange orders for cryptocurrencies. An order should specify the cryptocurrency type, amount, and whether it's a buy or sell order.
Orders should be market and limit.
You don't need to implement the integration with an external broker, mock data transfers.

**Order Matching**: Simulate a basic order matching system where buy and sell orders for the same asset or currency are automatically matched based on their amounts in the order book. Must include partial fulfillment.

# Optional Features:

**Transaction History**: Users should be able to view their transaction history, including deposits, withdrawals, and trades.

**Mock market data feed**: Integrate a simple mechanism to fetch and display mock real-time prices for fiat and cryptocurrencies.

# Important:

**Code quality** - we will be paying attention to how you write and organize the code
**Test coverage** - at least some key functionality should be covered