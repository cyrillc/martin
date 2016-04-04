USE plugins;

-- disable all constraints
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO
	example_call (example_call, description, function_id )
VALUES
	("Hello MArtIn", "Say Hello to MArtIn",11),
    ("Howdy Partner", "Say Hello to MArtIn",11),
    ("What's the weather today?", "Ask for current weather updates",13);
    
-- enable all constraints
SET FOREIGN_KEY_CHECKS = 1;
    
SELECT * FROM example_call;