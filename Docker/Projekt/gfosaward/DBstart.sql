GRANT ALL ON *.* TO 'henry'@'%' IDENTIFIED BY 'HhSveP12';

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Datenbank: `gfosaward`
--
CREATE schema gfosaward;
USE `gfosaward`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `GroupJoinRequest`
--

CREATE TABLE `GroupJoinRequest` (
  `GroupJoinRequestID` bigint(20) NOT NULL,
  `UserID` bigint(20) DEFAULT NULL,
  `WorkGroupID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Project`
--

CREATE TABLE `Project` (
  `ProjectID` bigint(20) NOT NULL,
  `CreatedDate` date DEFAULT NULL,
  `CreatorID` bigint(20) DEFAULT NULL,
  `DeadlineDate` date DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `SmtpConfig`
--

CREATE TABLE `SmtpConfig` (
  `SmtpConfigID` bigint(20) NOT NULL,
  `SenderMail` varchar(255) DEFAULT NULL,
  `SmtpHost` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Task`
--

CREATE TABLE `Task` (
  `TaskID` bigint(20) NOT NULL,
  `CreatedDate` date DEFAULT NULL,
  `DeadlineDate` date DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Score` bigint(20) DEFAULT NULL,
  `Status` int(11) DEFAULT NULL,
  `Creator` bigint(20) DEFAULT NULL,
  `ProjectID` bigint(20) DEFAULT NULL,
  `UserID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Team`
--

CREATE TABLE `Team` (
  `TeamID` bigint(20) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `WorkGroupID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

CREATE TABLE `User` (
  `UserID` bigint(20) NOT NULL,
  `Birthday` date DEFAULT NULL,
  `EMail` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Rank` int(11) DEFAULT NULL,
  `RegestrationDay` date DEFAULT NULL,
  `Score` bigint(20) DEFAULT NULL,
  `Verified` tinyint(1) DEFAULT '0',
  `TeamID` bigint(20) DEFAULT NULL,
  `WorkGroupID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `WorkAllocation`
--

CREATE TABLE `WorkAllocation` (
  `WA_ID` bigint(20) NOT NULL,
  `ProjectID` bigint(20) DEFAULT NULL,
  `UserID` bigint(20) DEFAULT NULL,
  `WorkGroupID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `WorkGroup`
--

CREATE TABLE `WorkGroup` (
  `WorkGroupID` bigint(20) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `SmtpConfigID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `GroupJoinRequest`
--
ALTER TABLE `GroupJoinRequest`
  ADD PRIMARY KEY (`GroupJoinRequestID`),
  ADD KEY `FK_GroupJoinRequest_UserID` (`UserID`),
  ADD KEY `FK_GroupJoinRequest_WorkGroupID` (`WorkGroupID`);

--
-- Indizes für die Tabelle `Project`
--
ALTER TABLE `Project`
  ADD PRIMARY KEY (`ProjectID`);

--
-- Indizes für die Tabelle `SmtpConfig`
--
ALTER TABLE `SmtpConfig`
  ADD PRIMARY KEY (`SmtpConfigID`);

--
-- Indizes für die Tabelle `Task`
--
ALTER TABLE `Task`
  ADD PRIMARY KEY (`TaskID`),
  ADD KEY `FK_Task_ProjectID` (`ProjectID`),
  ADD KEY `FK_Task_Creator` (`Creator`),
  ADD KEY `FK_Task_UserID` (`UserID`);

--
-- Indizes für die Tabelle `Team`
--
ALTER TABLE `Team`
  ADD PRIMARY KEY (`TeamID`),
  ADD KEY `FK_Team_WorkGroupID` (`WorkGroupID`);

--
-- Indizes für die Tabelle `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`UserID`),
  ADD KEY `FK_User_TeamID` (`TeamID`),
  ADD KEY `FK_User_WorkGroupID` (`WorkGroupID`);

--
-- Indizes für die Tabelle `WorkAllocation`
--
ALTER TABLE `WorkAllocation`
  ADD PRIMARY KEY (`WA_ID`),
  ADD KEY `FK_WorkAllocation_WorkGroupID` (`WorkGroupID`),
  ADD KEY `FK_WorkAllocation_ProjectID` (`ProjectID`),
  ADD KEY `FK_WorkAllocation_UserID` (`UserID`);

--
-- Indizes für die Tabelle `WorkGroup`
--
ALTER TABLE `WorkGroup`
  ADD PRIMARY KEY (`WorkGroupID`),
  ADD KEY `FK_WorkGroup_SmtpConfigID` (`SmtpConfigID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `GroupJoinRequest`
--
ALTER TABLE `GroupJoinRequest`
  MODIFY `GroupJoinRequestID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `Project`
--
ALTER TABLE `Project`
  MODIFY `ProjectID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `SmtpConfig`
--
ALTER TABLE `SmtpConfig`
  MODIFY `SmtpConfigID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `Task`
--
ALTER TABLE `Task`
  MODIFY `TaskID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `Team`
--
ALTER TABLE `Team`
  MODIFY `TeamID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `User`
--
ALTER TABLE `User`
  MODIFY `UserID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `WorkAllocation`
--
ALTER TABLE `WorkAllocation`
  MODIFY `WA_ID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `WorkGroup`
--
ALTER TABLE `WorkGroup`
  MODIFY `WorkGroupID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `GroupJoinRequest`
--
ALTER TABLE `GroupJoinRequest`
  ADD CONSTRAINT `FK_GroupJoinRequest_UserID` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`),
  ADD CONSTRAINT `FK_GroupJoinRequest_WorkGroupID` FOREIGN KEY (`WorkGroupID`) REFERENCES `WorkGroup` (`WorkGroupID`);

--
-- Constraints der Tabelle `Task`
--
ALTER TABLE `Task`
  ADD CONSTRAINT `FK_Task_Creator` FOREIGN KEY (`Creator`) REFERENCES `User` (`UserID`),
  ADD CONSTRAINT `FK_Task_ProjectID` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
  ADD CONSTRAINT `FK_Task_UserID` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`);

--
-- Constraints der Tabelle `Team`
--
ALTER TABLE `Team`
  ADD CONSTRAINT `FK_Team_WorkGroupID` FOREIGN KEY (`WorkGroupID`) REFERENCES `WorkGroup` (`WorkGroupID`);

--
-- Constraints der Tabelle `User`
--
ALTER TABLE `User`
  ADD CONSTRAINT `FK_User_TeamID` FOREIGN KEY (`TeamID`) REFERENCES `Team` (`TeamID`),
  ADD CONSTRAINT `FK_User_WorkGroupID` FOREIGN KEY (`WorkGroupID`) REFERENCES `WorkGroup` (`WorkGroupID`);

--
-- Constraints der Tabelle `WorkAllocation`
--
ALTER TABLE `WorkAllocation`
  ADD CONSTRAINT `FK_WorkAllocation_ProjectID` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
  ADD CONSTRAINT `FK_WorkAllocation_UserID` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`),
  ADD CONSTRAINT `FK_WorkAllocation_WorkGroupID` FOREIGN KEY (`WorkGroupID`) REFERENCES `WorkGroup` (`WorkGroupID`);

--
-- Constraints der Tabelle `WorkGroup`
--
ALTER TABLE `WorkGroup`
  ADD CONSTRAINT `FK_WorkGroup_SmtpConfigID` FOREIGN KEY (`SmtpConfigID`) REFERENCES `SmtpConfig` (`SmtpConfigID`);
COMMIT;
