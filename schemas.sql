-- drop existing table
DROP TABLE IF EXISTS Org, Meet, Participant, Leg, Event, Stroke,Distance,Heat,Swim, StrokeOf CASCADE;

----------------------------------Org----------------------------------
CREATE TABLE Org(
    id VARCHAR(50),
    name VARCHAR(50),
    is_univ BOOLEAN,
    PRIMARY KEY (id)
);

DROP FUNCTION IF EXISTS InsertOrg(id VARCHAR(50), name VARCHAR(50), is_univ BOOLEAN);
CREATE OR REPLACE FUNCTION InsertOrg(par_id VARCHAR(50), par_name varchar(50), par_is_univ BOOLEAN)
RETURNS VOID
AS $$
    BEGIN
        INSERT INTO Org VALUES(par_id, par_name, par_is_univ)
        ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, is_univ = EXCLUDED.is_univ;
    END; $$
LANGUAGE plpgsql;

----------------------------------Meet----------------------------------
CREATE TABLE Meet(
    name VARCHAR(50),
    start_date DATE,
    num_days INT, 
    org_id VARCHAR(50),
    PRIMARY KEY (name), 
    FOREIGN KEY(org_id) REFERENCES Org(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP FUNCTION IF EXISTS InsertMeet(n_name VARCHAR(50),n_start_date DATE, n_num_days INT, n_org_id VARCHAR(50));
CREATE OR REPLACE FUNCTION InsertMeet(n_name VARCHAR(50),n_start_date DATE, n_num_days INT, n_org_id VARCHAR(50))
RETURNS VOID
AS $$
    BEGIN
        INSERT INTO Meet VALUES
        	(n_name, n_start_date, n_num_days, n_org_id)
        ON CONFLICT (name) DO UPDATE SET start_date = EXCLUDED.start_date, num_days = EXCLUDED.num_days, org_id = EXCLUDED.org_id;
    END; $$
LANGUAGE plpgsql;
             
----------------------------------Participant----------------------------------
CREATE TABLE Participant(
    id VARCHAR(50), 
    gender VARCHAR(50), 
    org_id VARCHAR(50),
    name VARCHAR(50),
    PRIMARY KEY (id), 
    FOREIGN KEY (org_id) REFERENCES Org(id)
);

DROP FUNCTION IF EXISTS InsertParticipant(n_id VARCHAR(50), n_gender VARCHAR(50), n_org_id VARCHAR(50), n_name VARCHAR(50));
CREATE OR REPLACE FUNCTION InsertParticipant(n_id VARCHAR(50), n_gender VARCHAR(50), n_org_id VARCHAR(50), n_name VARCHAR(50))
RETURNS VOID
AS $$
    BEGIN
        INSERT INTO Participant VALUES(n_id, n_gender, n_org_id, n_name)
        ON CONFLICT (id) DO UPDATE SET gender = EXCLUDED.gender, org_id = EXCLUDED.org_id, name = EXCLUDED.name;
    END; $$
LANGUAGE plpgsql;

----------------------------------Leg----------------------------------
CREATE TABLE Leg(
    leg INT, 
    PRIMARY KEY (leg)
);

DROP FUNCTION IF EXISTS InsertLeg(n_leg INT);
CREATE OR REPLACE FUNCTION InsertLeg(n_leg INT)
RETURNS VOID
AS $$
    BEGIN
        INSERT INTO Leg VALUES (n_leg)
        ON CONFLICT (leg) DO NOTHING;
    END; $$
LANGUAGE plpgsql;       

----------------------------------Distance----------------------------------
CREATE TABLE Distance(
    distance INT, 
    PRIMARY KEY (distance) 
);

DROP FUNCTION IF EXISTS InsertDistance(n_distance INT);
CREATE OR REPLACE FUNCTION InsertDistance(n_distance INT)
RETURNS VOID
AS $$
    BEGIN
        INSERT INTO Distance VALUES (n_distance)
        ON CONFLICT (distance) DO NOTHING;
    END; $$
LANGUAGE plpgsql;
                
----------------------------------Stroke----------------------------------
CREATE TABLE Stroke(
    stroke VARCHAR(50), 
    PRIMARY KEY (stroke) 
);

DROP FUNCTION IF EXISTS InsertStroke(n_stroke VARCHAR(50));
CREATE OR REPLACE FUNCTION InsertStroke(n_stroke VARCHAR(50))
RETURNS VOID
AS $$
    BEGIN
        INSERT INTO Stroke VALUES (n_stroke)
        ON CONFLICT (stroke) DO NOTHING;
    END; $$
LANGUAGE plpgsql;

----------------------------------Event----------------------------------
CREATE TABLE Event(
    id VARCHAR(50), 
    gender VARCHAR(50), 
    distance INT, 
    PRIMARY KEY (id), 
    FOREIGN KEY (distance) REFERENCES Distance(distance)
);

DROP FUNCTION IF EXISTS InsertEvent(n_id VARCHAR(50), n_gender VARCHAR(50), n_distance INT);
CREATE OR REPLACE FUNCTION InsertEvent(n_id VARCHAR(50), n_gender VARCHAR(50), n_distance INT)
RETURNS VOID
AS $$
	BEGIN
		INSERT INTO Event VALUES (n_id, n_gender, n_distance)
		ON CONFLICT(id) DO UPDATE SET gender = EXCLUDED.gender, distance = EXCLUDED.distance;
	END;$$
LANGUAGE plpgsql;

----------------------------------StrokeOf----------------------------------
CREATE TABLE StrokeOf(
    event_id VARCHAR(50),
    leg INT,
    stroke VARCHAR(50),
    PRIMARY KEY (event_id, leg),
    FOREIGN KEY (event_id) REFERENCES Event (id),
    FOREIGN KEY (leg) REFERENCES Leg (leg),
    FOREIGN KEY (stroke) REFERENCES Stroke (stroke)
);

DROP FUNCTION IF EXISTS InsertStrokeOf(n_event_id VARCHAR(50), n_leg INT, n_stroke VARCHAR(50));
CREATE OR REPLACE FUNCTION InsertStrokeOf(n_event_id VARCHAR(50), n_leg INT, n_stroke VARCHAR(50))
RETURNS VOID
AS $$
	BEGIN
		INSERT INTO StrokeOf VALUES (n_event_id, n_leg, n_stroke)
		ON CONFLICT(event_id, leg) DO UPDATE SET stroke = EXCLUDED.stroke;
	END;$$
LANGUAGE plpgsql;

----------------------------------Heat----------------------------------
CREATE TABLE Heat(
    id VARCHAR(50), 
    event_id VARCHAR(50), 
    meet_name VARCHAR(50),
    PRIMARY KEY (id, event_id, meet_name),
    FOREIGN KEY (event_id) REFERENCES Event(id),
    FOREIGN KEY (meet_name) REFERENCES Meet(name)
);

DROP FUNCTION IF EXISTS InsertHeat(n_id VARCHAR(50), n_event_id VARCHAR(50), n_meet_name VARCHAR(50));
CREATE OR REPLACE FUNCTION InsertHeat(n_id VARCHAR(50), n_event_id VARCHAR(50), n_meet_name VARCHAR(50))
RETURNS VOID
AS $$
	BEGIN
		INSERT INTO Heat VALUES (n_id, n_event_id, n_meet_name)
		ON CONFLICT(id, event_id, meet_name) DO NOTHING;
	END;$$
LANGUAGE plpgsql;                

----------------------------------Swim----------------------------------
CREATE TABLE Swim(
    heat_id VARCHAR(50), 
    event_id VARCHAR(50), 
    meet_name VARCHAR(50),
    participant_id VARCHAR(50),
    leg INT,
    time FLOAT,
    PRIMARY KEY (heat_id, event_id, meet_name, participant_id),
    FOREIGN KEY (heat_id, event_id, meet_name) REFERENCES Heat(id, event_id, meet_name),
    FOREIGN KEY (participant_id) REFERENCES Participant(id),
    FOREIGN KEY (leg) REFERENCES Leg(leg)
);


DROP FUNCTION IF EXISTS InsertSwim(n_heat_id VARCHAR(50), n_event_id VARCHAR(50), n_meet_name VARCHAR(50),n_participant_id VARCHAR(50),n_leg INT,n_time FLOAT);
CREATE OR REPLACE FUNCTION InsertSwim(n_heat_id VARCHAR(50), n_event_id VARCHAR(50), n_meet_name VARCHAR(50),n_participant_id VARCHAR(50),n_leg INT,n_time FLOAT)
RETURNS VOID
AS $$
	BEGIN
		INSERT INTO Swim VALUES (n_heat_id, n_event_id, n_meet_name, n_participant_id, n_leg, n_time)
		ON CONFLICT(heat_id, event_id, meet_name, participant_id) DO UPDATE SET leg = EXCLUDED.leg, time = EXCLUDED.time;
	END;$$
LANGUAGE plpgsql;


/*
    Internal function used to compute the swim result with rank
 */
DROP FUNCTION IF EXISTS Compute();
CREATE OR REPLACE FUNCTION Compute()
RETURNS VOID
AS $$
    BEGIN
        -- select which heat is a relay heat
        DROP VIEW IF EXISTS Relay_event CASCADE;
        CREATE VIEW Relay_event AS
        SELECT DISTINCT heat_id, event_id, meet_name
        FROM Swim
        WHERE leg = 2;

        -- split the Relay_result
        DROP VIEW IF EXISTS Relay_result CASCADE;
        CREATE VIEW Relay_result AS
        SELECT 
            Swim.heat_id, 
            Swim.event_id, 
            Swim.meet_name, 
            Participant.org_id,
            SUM(time) AS relay_time,
            RANK() OVER (PARTITION BY (Swim.event_id, Swim.meet_name) ORDER BY SUM(time))
        FROM Swim
        INNER JOIN Relay_event
            ON Swim.heat_id = Relay_event.heat_id 
            AND Swim.event_id = Relay_event.event_id
            AND Swim.meet_name = Relay_event.meet_name
        INNER JOIN Participant
            ON Swim.participant_id = Participant.id
        GROUP BY Swim.heat_id, Swim.event_id, Swim.meet_name, Participant.org_id;

        -- insert relay_result into Swim
        DROP VIEW IF EXISTS Combined_result CASCADE;
        CREATE VIEW Combined_result AS
        SELECT heat_id, event_id, meet_name, participant_id, name, org_id, leg, time, relay_time,
            CASE 
                WHEN relay_time IS NULL THEN individual_rank
                ELSE rank
            END AS event_rank
        FROM (
            SELECT 
                Swim.heat_id,
                Swim.event_id,
                Swim.meet_name,
                Swim.participant_id,
                Participant.name,
                Participant.org_id,
                leg, time, Relay_result.relay_time,
                Relay_result.rank,
                RANK() OVER (PARTITION BY (Swim.event_id, Swim.meet_name) ORDER BY Swim.time) 
                    AS individual_rank
            FROM Swim
            INNER JOIN Participant ON Swim.participant_id = Participant.id
            LEFT JOIN Relay_result
                ON Swim.heat_id = Relay_result.heat_id
                AND Swim.event_id = Relay_result.event_id
                AND Swim.meet_name = Relay_result.meet_name
                AND Participant.org_id = Relay_result.org_id
            ) AS Temp;
    END;$$
LANGUAGE plpgsql;


/*
    For a Meet, display a Heat Sheet
 */
DROP FUNCTION IF EXISTS DisplayHeat(n_meet_name VARCHAR(50));
CREATE OR REPLACE FUNCTION DisplayHeat(n_meet_name VARCHAR(50))
RETURNS TABLE (
    heat_id VARCHAR(50), 
    event_id VARCHAR(50), 
    meet_name VARCHAR(50),
    participant_id VARCHAR(50),
    name VARCHAR(50),
    org_id VARCHAR(50),
    r_time FLOAT,
    r_relay_time FLOAT,
    r_rank BIGINT
)
AS $$
	BEGIN
        -- perform the internal function first to update the Combined_result View
        PERFORM Compute();

        RETURN QUERY 
            SELECT 
                Combined_result.heat_id, 
                Combined_result.event_id, 
                Combined_result.meet_name,
                Combined_result.participant_id,
                Combined_result.name,
                Combined_result.org_id,
                Combined_result.time,
                Combined_result.relay_time,
                Combined_result.event_rank
            FROM Combined_result
            WHERE Combined_result.meet_name = n_meet_name;
	END;$$
LANGUAGE plpgsql;


/*
	For a Participant and Meet, display a Heat Sheet limited to just that swimmer, including any relays they are in
 */
DROP FUNCTION IF EXISTS DisplayHeatOfMeetPlayer(n_meet_name VARCHAR(50), n_participant_id VARCHAR(50));
CREATE OR REPLACE FUNCTION DisplayHeatOfMeetPlayer(n_meet_name VARCHAR(50),n_participant_id VARCHAR(50))
RETURNS TABLE (
    heat_id VARCHAR(50), 
    event_id VARCHAR(50), 
    meet_name VARCHAR(50),
    participant_id VARCHAR(50),
    name VARCHAR(50),
    org_id VARCHAR(50),
    r_time FLOAT,
    r_relay_time FLOAT,
    r_rank BIGINT
)
AS $$
	BEGIN
        -- perform the internal function first to update the Combined_result View
        PERFORM Compute();

        RETURN QUERY 
            SELECT 
                Combined_result.heat_id, 
                Combined_result.event_id, 
                Combined_result.meet_name,
                Combined_result.participant_id,
                Combined_result.name,
                Combined_result.org_id,
                Combined_result.time,
                Combined_result.relay_time,
                Combined_result.event_rank
            FROM Combined_result
            WHERE Combined_result.meet_name = n_meet_name AND Combined_result.participant_id = n_participant_id;
    END;$$
LANGUAGE plpgsql;

/*
	For a School and Meet, display a Heat Sheet limited to just that School’s swimmers.
 */
DROP FUNCTION IF EXISTS DisplayHeatOfMeetSchool(n_meet_name VARCHAR(50),n_school_name VARCHAR(50));
CREATE OR REPLACE FUNCTION DisplayHeatOfMeetSchool(n_meet_name VARCHAR(100),n_school_name VARCHAR(50))
RETURNS TABLE (
    heat_id VARCHAR(50), 
    event_id VARCHAR(50), 
    meet_name VARCHAR(50),
    participant_id VARCHAR(50),
    name VARCHAR(50),
    org_id VARCHAR(50),
    r_time FLOAT,
    r_relay_time FLOAT,
    r_rank BIGINT
)
AS $$
	BEGIN
        -- perform the internal function first to update the Combined_result View
        PERFORM Compute();

        RETURN QUERY 
            SELECT 
                Combined_result.heat_id, 
                Combined_result.event_id, 
                Combined_result.meet_name,
                Combined_result.participant_id,
                Combined_result.name,
                Combined_result.org_id,
                Combined_result.time,
                Combined_result.relay_time,
                Combined_result.event_rank
            FROM Combined_result
            INNER JOIN Participant ON Participant.id = Combined_result.participant_id
            INNER JOIN Org ON Participant.org_id = Org.id
            WHERE Org.name = n_school_name AND Combined_result.meet_name = n_meet_name;
    END;$$
LANGUAGE plpgsql;

/*
	For a School and Meet, display just the names of the competing swimmers.
 */
DROP FUNCTION IF EXISTS DisplayNameBySchool(n_meet_name VARCHAR(50),n_school_name VARCHAR(50));
CREATE OR REPLACE FUNCTION DisplayNameBySchool(n_meet_name VARCHAR(100),n_school_name VARCHAR(50))
RETURNS TABLE (r_participant_name VARCHAR(50))
AS $$
	BEGIN
		RETURN QUERY 
			SELECT DISTINCT Participant.name
			FROM Swim
			INNER JOIN Participant ON Participant.id = Swim.participant_id
			INNER JOIN Org ON Participant.org_id = Org.id
			WHERE Org.name = n_school_name AND swim.meet_name = n_meet_name;
    END;$$
LANGUAGE plpgsql;

/*
	For an Event and Meet, display all results sorted by time. Include the heat, swimmer(s) name(s), and rank
 */
DROP FUNCTION IF EXISTS DisplayResultByEventMeet(n_meet_name VARCHAR(50),n_event_name VARCHAR(50));
CREATE OR REPLACE FUNCTION DisplayResultByEventMeet(n_meet_name VARCHAR(100), n_event_name VARCHAR(50))
RETURNS TABLE (
    r_heat_id VARCHAR(50),
    r_swimmer_name VARCHAR(50),
    r_rank BIGINT
)
AS $$
    BEGIN
        -- perform the internal function first to update the Combined_result View
        PERFORM Compute();

        RETURN QUERY 
            SELECT heat_id, name, event_rank
            FROM Combined_result
            WHERE Combined_result.event_id = n_event_name AND Combined_result.meet_name = n_meet_name;
    END;$$
LANGUAGE plpgsql;

/*
    Rank Org by Meet
 */
DROP FUNCTION IF EXISTS DisplayRank(n_meet_name VARCHAR(50));
CREATE OR REPLACE FUNCTION DisplayRank(n_meet_name VARCHAR(50))
RETURNS TABLE (
    r_meet_name VARCHAR(50),
    r_org_id VARCHAR(50),
    r_org_name VARCHAR(50),
    r_org_score BIGINT,
    r_rank BIGINT
)
AS $$
    BEGIN
        -- perform the internal function first to update the Combined_result View
        PERFORM Compute();

        DROP VIEW IF EXISTS Relay_score;
        CREATE VIEW Relay_score AS
        SELECT meet_name, org_id, SUM(Temp.relay_score) AS org_relay_score
        FROM (
            SELECT *, 
                CASE
                    WHEN rank = 1 THEN 8
                    WHEN rank = 2 THEN 4
                    WHEN rank = 3 THEN 2
                    ELSE 0
                END AS relay_score
            FROM Relay_result
        ) AS Temp
        GROUP BY (meet_name, org_id);

        DROP VIEW IF EXISTS Individual_score;
        CREATE VIEW Individual_score AS
        SELECT meet_name, org_id, SUM(Temp.individual_score) AS org_individual_score
        FROM (
            SELECT *, 
                CASE
                    WHEN relay_time IS NULL AND event_rank = 1 THEN 6
                    WHEN relay_time IS NULL AND event_rank = 2 THEN 4
                    WHEN relay_time IS NULL AND event_rank = 3 THEN 3
                    WHEN relay_time IS NULL AND event_rank = 4 THEN 2
                    WHEN relay_time IS NULL AND event_rank = 5 THEN 1
                    ELSE 0
                END AS individual_score
            FROM Combined_result
        ) AS Temp
        GROUP BY (meet_name, org_id);

        RETURN QUERY 
            SELECT meet_name, org_id, Org.name, org_score, RANK() OVER (PARTITION BY (meet_name) ORDER BY org_score DESC)
            FROM (
                SELECT Individual_score.meet_name, Individual_score.org_id,
                    CASE
                        WHEN Relay_score.org_relay_score IS NOT NULL THEN org_individual_score + Relay_score.org_relay_score
                        ELSE org_individual_score
                    END AS org_score
                FROM Individual_score
                LEFT JOIN Relay_score
                ON Individual_score.meet_name = Relay_score.meet_name 
                AND Individual_score.org_id = Relay_score.org_id
                ) AS Temp
            INNER JOIN Org ON org_id = Org.id
            WHERE meet_name = n_meet_name;
    END;$$
LANGUAGE plpgsql;

