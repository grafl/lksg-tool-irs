-- drop view lksg_tool_irs.v_analysis_report;
-- drop table lksg_tool_irs.investigation_bpns;
-- drop table lksg_tool_irs.bpns;
-- drop table lksg_tool_irs.analyses;
-- drop table lksg_tool_irs.investigations;
-- drop table lksg_tool_irs.flyway_schema_history;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS lksg_tool_irs AUTHORIZATION lksg_tool;

CREATE TABLE lksg_tool_irs.investigations (
    id character varying(64) NOT NULL,
    bom_lifecycle character varying(16) NOT NULL,
    callback_url character varying(1024) NOT NULL,
    global_asset_id character varying(64) NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS lksg_tool_irs.investigations OWNER to lksg_tool;
-- -----------------------------------------------------------------------------
CREATE TABLE lksg_tool_irs.bpns (
    bpn character varying(64) NOT NULL,
    PRIMARY KEY (bpn)
);
ALTER TABLE IF EXISTS lksg_tool_irs.bpns OWNER to lksg_tool;
-- -----------------------------------------------------------------------------
CREATE TABLE lksg_tool_irs.investigation_bpns (
    investigation character varying(64) NOT NULL,
    bpn character varying(64) NOT NULL,
    PRIMARY KEY (investigation, bpn)
);
ALTER TABLE IF EXISTS lksg_tool_irs.investigation_bpns OWNER to lksg_tool;
ALTER TABLE IF EXISTS lksg_tool_irs.investigation_bpns
    ADD CONSTRAINT fk_cbs_investigation FOREIGN KEY (investigation)
        REFERENCES lksg_tool_irs.investigations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;
ALTER TABLE IF EXISTS lksg_tool_irs.investigation_bpns
    ADD CONSTRAINT fk_cbs_bpn FOREIGN KEY (bpn)
        REFERENCES lksg_tool_irs.bpns (bpn) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;
-- -----------------------------------------------------------------------------
CREATE TABLE lksg_tool_irs.analyses (
     id character varying(64) NOT NULL,
     investigation character varying(64) NOT NULL,
     state character varying(18) NOT NULL,
     impacted character varying(18),
     aspect character varying(64),
     PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS lksg_tool_irs.analyses OWNER to lksg_tool;
ALTER TABLE IF EXISTS lksg_tool_irs.analyses
    ADD CONSTRAINT fk_cbs_investigation FOREIGN KEY (investigation)
        REFERENCES lksg_tool_irs.investigations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW lksg_tool_irs.v_analysis_report AS
select a.investigation,
       i.global_asset_id,
       ib.bpn,
       a.state,
       case a.state
           when 'COMPLETED' then 'btn-success'
           when 'CANCELED' then 'btn-dark'
           when 'ERROR' then 'btn-danger'
           when 'RUNNING' then 'btn-warning'
           when 'UNSAVED' then 'btn-secondary'
           when 'TRANSFERS_FINISHED' then 'btn-info'
           else 'btn-primary' -- INITIAL
        end button_state,
       a.impacted
  from lksg_tool_irs.analyses a,
       lksg_tool_irs.investigations i,
       lksg_tool_irs.investigation_bpns ib
 where a.investigation = i.id
   and i.id = ib.investigation
;
