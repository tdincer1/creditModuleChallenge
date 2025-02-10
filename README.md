# Credit Module

Spring Boot backend Loan API for a bank so that their employees can
create, list and pay loans for their customers.
Storing data on in-memory H2 Database.

<!-- TOC -->
* [Credit Module](#credit-module)
  * [Technical Design](#technical-design)
    * [Table Design](#table-design)
    * [Endpoints](#endpoints)
    * [Potential Test Cases](#potential-test-cases)
  * [How to use (TODO)](#how-to-use-todo)
  * [Additional notes](#additional-notes)
<!-- TOC -->

## Technical Design
### Table Design
* Customer: id, name, surname, creditLimit, usedCreditLimit
* Loan: id, customerId, loanAmount, numberOfInstallment, createDate, isPaid
* LoanInstallment: id, loanId, amount, paidAmount, dueDate, paymentDate, isPaid


### Endpoints
* createLoan
  * Request:
    * long customerId
    * Double amount
    * Double interestRate (between 0.1 – 0.5)
    * int numberOfInstallments (6, 9, 12, 24)
  * Response
    * int responseCode: (0: successfully created, etc.)
    * String responseDescription
    
* listLoans
  * Request:
    * long customerId
    * String loanStatus (String Enum: PAID, UNPAID, ALL)
  * Response
    * int responseCode: (0: successfully created, etc.)
    * String responseDescription
    * List<Loan>:
      * String loanStatus
      * long loanId (used for differentiate loans and inquiring loan's installments.)
      * Double loanAmount
      * int numberOfInstallments
    
* listInstallment
  * Request:
    * long customerId
    * long loanId
  * Response:
    * int responseCode: (0: successfully created, etc.)
    * String responseDescription
    * List<Installment>
      * long loanId
      * Double Amount
      * String dueDate
      * String paymentDate
      * Boolean isPaid
    
* payLoan
  * Request
    * long customerId
    * İnt loanId
    * Double Amount
  * Response
    * int responseCode: (0: successfully created, etc.)
    * String responseDescription
    * int countOfPaidInstallments
    * Double paidAmount (amount - remaining)
    * Boolean loanClosed

### Potential Test Cases
* createLoan
  * Unit:
    * Limit not sufficient case
    * Create loan successfully
      * Verify usedCreditLimit update on Customer table
      * Verify insert to Loan Table
      * Verify insert to LoanInstallment Table
  * Integration:
    * Number of installments check
    * Interest rate check
    * Customer not found
* listLoans
  * Unit:
    * Customer not found
    * Matching loan not found
  * Integration:
    * Invalid loan status
* listInstallment
  * Unit:
    * Limit not sufficient case
  * Integration:
    * Loan not found
    * Customer not found
* payLoan
  * Unit:
    * Not paid any installments
      * Verify no update on Customer, Loan, LoanInstallment tables.
    * payAll
      * Verify update on Customer, Loan, LoanInstallment tables.
    * paySomeWithRemainderAmountAfter
      * Verify update on Customer, LoanInstallment tables.
    * paySomeUpTo3Months
      * Verify update on Customer, LoanInstallment tables.
  * Integration:
    * Loan not found
    * Customer not found

## How to use (TODO)
## Additional notes