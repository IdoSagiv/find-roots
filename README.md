# Find Roots
An Android exercise for developers teaching how to play around with intents, activities,
services and broadcast receivers

## Answer to question
In order to  let the service run for maximum 200ms in tests environments, but continue to run for
20sec max in the real app (production environment) we can add another 'extra' from the tests to the to the
intent that calls the service with the number of milliseconds to wait until abort, and in the
service check if this extra exists and if not set the default value to 20 seconds.
## App's runtime diagram:
![project diagram](project_diagram.png)

## Ethical pledge
I pledge the highest level of ethical principles in support of academic excellence.
I ensure that all of my work reflects my own abilities and not those of someone else.