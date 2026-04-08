# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM jboss/wildfly
ADD target/gfosaward-1.0.war /opt/jboss/wildfly/standalone/deployments/
