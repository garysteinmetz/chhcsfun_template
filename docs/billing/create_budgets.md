# Create Daily and Monthly Budgets to Send Email Alerts If Too Much Money Is Being Spent

AWS supports the creation of budgets which can send alerts when more than a certain amount
of money is being charged over a period of time. Create two reports - daily and monthly -
to catch surprise expenditures early.

These two reports are recommended because the registration or reregistration of a domain
name could cause a spike both in daily and monthly expenses that just lasts a day.
Having these two reports should allow both detection of a one-time spike in costs as
well as a higher-than-normal accumulation of expenses over a longer period (i.e. month).

Follow these instructions for each of the (2) reports - 'Daily' and 'Monthly'.

  - Go to https://console.aws.amazon.com/billing/home
  - Click the 'Budgets' link in the left column
  - Click the 'Create a budget' button on the right
  - Keep the 'Cost budget' radio button selected then click the 'Set your budget' button
  - Enter a value in the 'Name' field
    - Daily - CHHCS Fun App Daily Budget
    - Monthly - CHHCS Fun App Monthly Budget
  - Select a value in the 'Period' dropdown box
    - Daily - Daily
    - Monthly - Monthly
  - Enter a value in the 'Budgeted amount' field (these are example recommended values)
    - Daily - 15
    - Monthly - 20
  - Click the 'Configure thresholds' button located in the bottom right
  - In the 'Alert threshold' text box, enter 100
  - In the 'Email recipients' text area, enter a comma-separated list of the email
  addresses that should receive an alert
  - Click the 'Confirm budget' button in the lower right
  - Review your settings then click the 'Create' button in the lower right
