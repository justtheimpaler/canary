# Canary

Canary finds subfolders with too many files starting from a base folder. Its goal is to detect if a folders that are growing too much and can make the application slow, or worse can start crashing applications.

Canary can be run as an automated process periodically and connected to other appliances to produce alerts when findings are encountered, so silently growing folders are noticed.

## Defaults

By default it points out any folder with more than 10 thousand files/subfolders in to. This number can be superseded in the command line with a different value. For the purpose
of the count &mdash; and the alert &mdash; the count doesn't make any difference between files or subfolder in a folder; both are considered "file entries" and count towards the threshold.

This limit errs in the side of caution. Local drives can typically absorb much higher loads, while networked drives have lower capacities. In the current cloud solutions, this 
limitation is further lowered by the cheaper and cheaper commodity hardware solutions that are terrible attractive to IT managers, but at the same time, can include silent limitations.

By default it excludes from the search any subfolder which name ends with "_attic". This is to allow the system admins to have an opportunity to move files out of the way temporarily before their
official deletion. Canary considers these attic folders as *managed* so there's no need to raise an alert. Nevertheless, the attic can be disabled, as needed.

## Usage

TBD


