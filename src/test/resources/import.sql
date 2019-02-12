INSERT INTO test.room(id, game_rules, "name") VALUES (1,1,'bro club');
INSERT INTO test.room(id, game_rules, "name") VALUES (2,1,'House of spades');
INSERT INTO test.room(id, game_rules, "name") VALUES (3,0,'house of blinds');
INSERT INTO test.room(id, game_rules, "name") VALUES (4,2,'poker plaza');
INSERT INTO test.room(id, game_rules, "name") VALUES (5,0,'flop house');
INSERT INTO test.room(id, game_rules, "name") VALUES (6,2,'wizard den');
INSERT INTO test.room(id, game_rules, "name") VALUES (7,2,'las vegas');
INSERT INTO test.room(id, game_rules, "name") VALUES (8,1,'catchers club');
INSERT INTO test.room(id, game_rules, "name") VALUES (9,2,'singapore');

INSERT INTO test.round (id,button,current_phase,is_finished,pot,room_id) VALUES (602,1,0,true,0,1);
INSERT INTO test.round (id,button,current_phase,is_finished,pot,room_id) VALUES (618,2,0,false,450,1);

INSERT INTO test.card (id,"type",round_id) VALUES (608,24,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (609,25,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (610,0,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (611,31,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (612,14,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (613,10,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (614,11,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (615,17,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (616,8,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (617,50,NULL);
INSERT INTO test.card (id,"type",round_id) VALUES (619,26,618);
INSERT INTO test.card (id,"type",round_id) VALUES (620,19,618);
INSERT INTO test.card (id,"type",round_id) VALUES (621,32,618);
INSERT INTO test.card (id,"type",round_id) VALUES (622,27,618);
INSERT INTO test.card (id,"type",round_id) VALUES (623,30,618);

INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (588,2400,0,true,true,1,'4aa8a441-82cb-409b-8952-61120a5bded4',612,613,1,618);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1147,2500,0,false,true,0,'c8deb0ec-6e1b-4e7a-884e-4eda096a09b1',NULL,NULL,8,NULL);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1148,2500,0,false,true,0,'8b4ac4cb-62ac-4947-8ddc-fb4f874e1846',NULL,NULL,8,NULL);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (589,2500,0,true,true,0,'4aa8a441-82cb-409b-8952-61120a5bded4',614,615,1,618);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1149,2500,0,false,true,0,'f1a297b5-2bc3-45da-bcd8-4018f9a2b64d',NULL,NULL,8,NULL);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1150,2500,0,false,true,0,'c8deb0ec-6e1b-4e7a-884e-4eda096a09b1',NULL,NULL,8,NULL);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1151,2500,0,false,true,0,'f1a297b5-2bc3-45da-bcd8-4018f9a2b64d',NULL,NULL,8,NULL);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1152,500,0,false,true,0,'f1a297b5-2bc3-45da-bcd8-4018f9a2b64d',NULL,NULL,3,NULL);
INSERT INTO test.player (id,chip_count,hand_type,in_round,is_active,last_act,user_id,first_card,second_card,room_id,roud_id) VALUES (1153,500,0,false,true,0,'c8deb0ec-6e1b-4e7a-884e-4eda096a09b1',NULL,NULL,3,NULL);


INSERT INTO test.act (id,bet,phase,"type",player,round_id) VALUES (1086,25,0,1,588,618);
INSERT INTO test.act (id,bet,phase,"type",player,round_id) VALUES (1129,25,0,1,588,618);
INSERT INTO test.act (id,bet,phase,"type",player,round_id) VALUES (1145,25,0,1,588,618);

DROP SEQUENCE test.hibernate_sequence;
--
CREATE SEQUENCE test.hibernate_sequence INCREMENT BY 1 MINVALUE 10000 MAXVALUE 9223372036854775807 CACHE 1 NO CYCLE;
--
-- -- Permissions
--
ALTER SEQUENCE test.hibernate_sequence OWNER TO "ip2Master";
GRANT ALL ON SEQUENCE test.hibernate_sequence TO "ip2Master";