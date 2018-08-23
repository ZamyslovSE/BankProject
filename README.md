Bank project with Spring Boot and MySQL database. Server is started on port 8443 (config in resources).
API for data access:
- /register (passport, password , first_name, last_name, phone_nmuber)
- /login (passport, password)
- /deleteUser (id, kak ego dostavat menya poka ne volnuet)
- /addFunds (passport, amount)
- /withdrawFunds (passport, amount)
- /transferFunds (sender_passport, targer_passport, amount)
- /checkBalance (passport)
