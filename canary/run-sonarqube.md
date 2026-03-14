To update the Sonarqub report:

    mvn clean verify sonar:sonar -Dsonar.projectKey=canary-1 -Dsonar.projectName='canary-1' -Dsonar.host.url=http://192.168.56.244:9000 -Dsonar.token=sqp_0957675fe48cb77ff8eb396e5d0207f34bcee84a

See the report at:

     http://192.168.56.244:9000/dashboard?id=hotrod

(use admin / admin1)
