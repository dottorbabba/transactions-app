# transactions-app
Coding challenge

##Assumptions
During the design of the application I assumed that the sequence of PUT requests is used to build the tree adding a leaf at a time and never changing the relationship between a transaction and its parent after the node creation (but I haven't enforced it with a check into the code given the exercise nature of the coding).

That's the reason why I adopted the "Path Enumeration" as tree representation pattern.
"Path Enumeration" is very efficient in insertion of a new node as a leaf and in traversing the tree. It performs with a higher complexity when an internal node changes its position into the tree structure or a new node is inserted in the middle of the tree.

##Installation
```
git clone https://github.com/dottorbabba/transactions-app.git
cd transactions-app
mvn spring-boot:run
```
##Test
```
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X PUT -d '{ "amount": 5000, "type": "cars" }' http://localhost:8080/transactionservice/transaction/10

curl -v -H "Accept: application/json" -H "Content-type: application/json" -X PUT -d '{ "amount": 10000, "type": "shopping", "parent_id": 10 }' http://localhost:8080/transactionservice/transaction/11

curl -v http://localhost:8080/transactionservice/types/cars

curl -v http://localhost:8080/transactionservice/sum/10

curl -v http://localhost:8080/transactionservice/sum/11
```

##Asymptotic behaviour

The transactions hierarchy is represented using the "Path Enumeration" pattern. It performs in linear time in tree traversal and constant time in leaf addition.
If a node changes its position inside the tree or is inserted inside the tree a different tree representation should be evaluated.

Depends on the number of writings respects of the number of readings, the reading part of the API (GET) can adopt caching to improve the response time (If readings are order of magnitude bigger than writings).

The "Types" API implementation can evolve adopting pagination to define a constant bytes transfer.

The "Sum" API can improve its performance in two different ways:
- adding to each node the total amount of its subtree (computed at each writing process into the node subtree) allows to get a constant time in sum calculation during reading (in this case changes in the middle of the tree structure are impacting)
- or splitting the computation in the sum of n subtrees and parallelizing the computation of each subtree (computed during read request).
