# Canary

Canary searches for folders that can have a growing number of files and subfolders in them. Its goal is to detect if folders are silently growing beyond a predefined size threshold, something that could potentially make one or more applications slow or even crash.

Canary can be run discretionally or periodically as an automated process. It scans all the subfolders of one or more root folder, according to the configuration, and logs warnings and critical messages when they exceed the threshold specified in the rules. This log could potentionally be connected to monitoring tools to produce alerts for these findings.

## Setup

To use this tool you need:

- Access to the command line.
- Java 8 or newer installed.
- A folder (the working folder) where to install the tool and the configuration file.

To use it follow the steps:

### Step 1 - Download This Tool

Get the tool from Maven Central at [search results](https://central.sonatype.com/search?q=canary) and place it in the work folder.

Copy the JAR file into the working folder.

### Step 2 - Add Configuration File

The configuration is specified using YAML format. The default name of the configuration file is `canary.yaml`. This file will define one or more base folders to scan (the "root" folders) and their properties.

Starting in these folders Canary will scan all its subfolders to count file entries (i.e. files and subfolders). The configuration can specify one or more exclusion rules for this scan; these exclusion rules can take the form of:

- A regular expression to match a subfolder's name
- A relative path for a specific folder

For example, if we wanted to scan starting from two root folders (`/var/apps/myfolder` and `/opt/shared/logs`), the configuration file can take the form:

```yaml
---
root: /var/apps/myfolder
---
root: /opt/shared/logs
```

As stated before, each entry can be customized with:

- The regular expression pattern to exclude subfolder by name
- The path of specific subfolders
- The warning count threshold
- The critical count threshold

The following table define their details:

| Property | Description | Default |
| --  | -- | -- |
| root  | The root folder where the scan starts. All subfolders of it will be scanned, safe for subfolders that match the exclusion rules, if any | -- |
| excludeNamePatterns | Optional. The list of regular expression patterns to match to the subfolders. If a subfolder name matches one patter it's ignored | No subfolders are ignored |
| excludePaths | Optional. The list of paths to ignore. These paths are relative to the correspondig root folder | No paths are ignored |
| warningThreshold  | Optional. The warning count. Any folder or subfolder that exceeds this count will trigger a warning message | 5000 |
| criticalThreshold  | Optional. The critical count. Any folder or subfolder that exceeds this count will trigger a critical message  | 10000 |

As specified above, only the `root` property is mandatory for each root folder configuration.

**Note**: Since the configuration file is a YAML file, the properties can be escaped using the YAML syntax rules. This can be especially useful if the folders include non-typical characters in their names, such as (, ), =, :, ;, etc.

The default thresholds err in the side of caution. Local drives can typically absorb much higher loads, while networked drives have lower capacities. Nowadays, with the current cloud solutions, this 
limitation is further lowered by the cheaper and cheaper commodity hardware options that are terrible attractive to IT managers, but at the same time, can have silent limitations.

We could tweak the example above with a full configuration for the first root folder. We can decide to:

- Exclude any subfolders that end in `backup` or start with `attic`
- Exclude the subfolders `prod1/data1`, `prod2/data2`, and `prod3/data3`
- Define the warning count as 2000 and the critical count as 6000.

In this case, the configuration could take the form:

```yaml
---
root: /var/apps/myfolder
excludeNamePatterns: [ .*backup, attic.* ]
excludePaths: [ prod1/data1, prod2/data2, prod3/data3 ]
warningThreshold: 2000
criticalThreshold: 6000
---
root: /opt/shared/logs
```

In this example we specified all the properties for the first root folder, while the second use use the default values for them.

## Running This Tool

To scan the folders specified in the configuration, use the command line and go to the working folder. While in it run Canary, as in:

```bash
java -jar canary-1.0.0.jar
```

It will start scanning all root folders and subfolders and will show any warning and critical alerts, as in:

```log
2026-03-10 18:44:59.959 INFO   - Canary 1.0.0 - build 20260310-224457
2026-03-10 18:45:00.056 INFO   - Loaded configuration from: canary.yaml (2 root folders to scan)
2026-03-10 18:45:00.059 INFO   - Scanning root folder: /var/apps/myfolder
2026-03-10 18:45:00.060 WARN   - The folder /var/apps/myfolder/test/resources/com/itextpdf/text/pdf/pdfcleanup has 3156 files in it, and that exceeds the warning threshold of 2000.
2026-03-10 18:45:00.061 INFO   -  - Stats: counted 1248 subfolders and 23384 files --  with 0 critical and 1 warnings
2026-03-10 18:45:00.062 SEVERE - The folder /opt/shared/logs/fonts/cmaps has 17605 files in it, and that exceeds the critical threshold of 10000.
2026-03-10 18:45:00.099 INFO   -  - Stats: counted 7331 subfolders and 57255 files --  with 1 critical and 0 warnings
2026-03-10 18:45:00.099 INFO   - Grand total of 1 critical and 1 warnings -- Scanned 8589 subfolders and 80639 files -- Scan time: 00:00:00.040
```

If we wanted to specify a different configuration file we can do so by indicating it as a parameter in the command line:

```bash
java -jar canary-1.0.0.jar ./config/data-rules.yaml
```

## A Final Note

You can run this tool interactively a will at any time as shown above.

Typically the scanning is run periodically, maybe once a day or once a week. The resulting log file can be fed to a log monitor tool where alert rules can be set to identify WARN and SEVERE entries that indicate warning and critical threshold are reached. These alerts can be sent to a list of recipients that can take appropriate actions as needed.





