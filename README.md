# Canary

Canary finds subfolders with too many files starting from a base folder. Its goal is to detect if folders are silently growing beyond a predefined size threshold, something that could potentially make one or more applications slow or even crash.

Canary can be run as an automated process periodically to log warnings and critical levels for one or more folder roots. This log can be connected to monitoring tools to produce alerts for these findings.

## Defaults

By default it points out any folder with more than 10 thousand files/subfolders in to. This number can be superseded in the command line with a different value. For the purpose
of the count &mdash; and the alert &mdash; the count doesn't make any difference between files or subfolder in a folder; both are considered "file entries" and count towards the threshold.

This limit errs in the side of caution. Local drives can typically absorb much higher loads, while networked drives have lower capacities. In the current cloud solutions, this 
limitation is further lowered by the cheaper and cheaper commodity hardware solutions that are terrible attractive to IT managers, but at the same time, can include silent limitations.

By default it excludes from the search any subfolder which name ends with "_attic". This is to allow the system admins to have an opportunity to move files out of the way temporarily before their
official deletion. Canary considers these attic folders as *managed* so there's no need to raise an alert. Nevertheless, the attic can be disabled, as needed.

## Usage

TBD


