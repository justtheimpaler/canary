# Canary

Canary finds subfolders with too many files starting from a base folder. Its goal is to detect if a folders that are growing too much and can make the application slow, or worse can start crashing applications.

Canary can be run as an automated process periodically and connected to other appliances to produce alerts when findings are encountered, so silently growing folders are noticed.

# Defaults

By default it points out any folder with more than 10 thousand files/subfolders in to. This number can be superseded in the command line with a different value. For the purpose
of the count &mdash; and the alert &mndash; the count doesn't make any difference between files or subfolder in a folder; both are considered "file entries" and count towards the threshold.

By default it excludes from the search any subfolder which name ends with "_attic". This is to allow the system admins to have an opportunity to move files out of the way temporarily before their
official deletion. Canary considers these attic folders as *managed* so there's no need to raise an alert. Nevertheless, the attic can be disabled, as needed.


