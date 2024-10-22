# JavaTechnicalTest

# A default user has already been created, user details can be found in Data.sql in resources folder.

#Below are the API's call method, URL and request body required for each task.

Task 2: GET http://localhost:8080/api/bestprice?tradePair=BTCUSDT

Task 3: POST http://localhost:8080/api/trade/processtrade?userId=1
RequestBody
{
  "tradePair": "ETHUSDT",
  "amount": 1,
  "tradeType": "BUY",
  "baseCurrency": "ETH",
  "quoteCurrency": "USDT"
}

Task 4: GET http://localhost:8080/api/wallets/balances?userId=1

Task 5: GET http://localhost:8080/api/trade/history?userId=1