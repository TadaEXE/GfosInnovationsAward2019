# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM mysql:5.5

#CMD ["GRANT", "ALL", "PRIVILEGES", "ON", "*.*", "TO", "'henry'@'%'", "IDENTIFIED", "BY", "'HhSveP12'"];
ADD DBstart.sql /docker-entrypoint-initdb.d/
