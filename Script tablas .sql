prompt PL/SQL Developer Export Tables for user CLINICA@LOCALHOST:1521/FREE
prompt Created by Marvin on domingo, 9 de noviembre de 2025
set feedback off
set define off

prompt Dropping CLINICA...
drop table CLINICA cascade constraints;
prompt Dropping PACIENTE...
drop table PACIENTE cascade constraints;
prompt Dropping CITA...
drop table CITA cascade constraints;
prompt Creating CLINICA...
create table CLINICA
(
  id_clinica     NUMBER(15) not null,
  nombre         VARCHAR2(100) not null,
  direccion      VARCHAR2(150),
  telefono       VARCHAR2(20),
  correo         VARCHAR2(100),
  fecha_registro DATE default SYSDATE
)
tablespace TCLINICA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table CLINICA
  add primary key (ID_CLINICA)
  using index
  tablespace TCLINICA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating PACIENTE...
create table PACIENTE
(
  id_paciente    NUMBER(15) not null,
  nombre         VARCHAR2(100) not null,
  apellido       VARCHAR2(100) not null,
  fecha_nac      DATE,
  telefono       VARCHAR2(20),
  correo         VARCHAR2(100),
  fecha_registro DATE default SYSDATE
)
tablespace TCLINICA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table PACIENTE
  add primary key (ID_PACIENTE)
  using index
  tablespace TCLINICA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt Creating CITA...
create table CITA
(
  id_cita     NUMBER(15) not null,
  id_paciente NUMBER(15) not null,
  id_clinica  NUMBER(15) not null,
  fecha_cita  DATE not null,
  hora_inicio VARCHAR2(5) not null,
  hora_fin    VARCHAR2(5) not null,
  estado      VARCHAR2(20) default 'PROGRAMADA',
  descripcion VARCHAR2(200)
)
tablespace TCLINICA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table CITA
  add primary key (ID_CITA)
  using index
  tablespace TCLINICA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table CITA
  add constraint FK_CITA_CLINICA foreign key (ID_CLINICA)
  references CLINICA (ID_CLINICA);
alter table CITA
  add constraint FK_CITA_PACIENTE foreign key (ID_PACIENTE)
  references PACIENTE (ID_PACIENTE);

prompt Disabling triggers for CLINICA...
alter table CLINICA disable all triggers;
prompt Disabling triggers for PACIENTE...
alter table PACIENTE disable all triggers;
prompt Disabling triggers for CITA...
alter table CITA disable all triggers;
prompt Disabling foreign key constraints for CITA...
alter table CITA disable constraint FK_CITA_CLINICA;
alter table CITA disable constraint FK_CITA_PACIENTE;
prompt Loading CLINICA...
insert into CLINICA (id_clinica, nombre, direccion, telefono, correo, fecha_registro)
values (1, 'Clínica Central', 'San José Centro', '2255-4444', 'central@clinica.com', null);
insert into CLINICA (id_clinica, nombre, direccion, telefono, correo, fecha_registro)
values (1761505557966, 'ebais', 'una', '2778778', 'ebais', null);
commit;
prompt 2 records loaded
prompt Loading PACIENTE...
insert into PACIENTE (id_paciente, nombre, apellido, fecha_nac, telefono, correo, fecha_registro)
values (1, 'Jafet', 'Ruiz', to_date('14-08-2003', 'dd-mm-yyyy'), '8888-9999', 'jafet@example.com', to_date('25-10-2025', 'dd-mm-yyyy'));
insert into PACIENTE (id_paciente, nombre, apellido, fecha_nac, telefono, correo, fecha_registro)
values (1761505535558, 'Marvin', 'Torres', to_date('26-10-2025', 'dd-mm-yyyy'), '8554778', 'hola', to_date('26-10-2025 13:05:35', 'dd-mm-yyyy hh24:mi:ss'));
insert into PACIENTE (id_paciente, nombre, apellido, fecha_nac, telefono, correo, fecha_registro)
values (5171091613, 'Daniela', 'Mora', to_date('11-10-1999 18:00:00', 'dd-mm-yyyy hh24:mi:ss'), '88887777', 'dani@clinica.com', to_date('26-10-2025 20:16:21', 'dd-mm-yyyy hh24:mi:ss'));
commit;
prompt 3 records loaded
prompt Loading CITA...
insert into CITA (id_cita, id_paciente, id_clinica, fecha_cita, hora_inicio, hora_fin, estado, descripcion)
values (1, 1, 1, to_date('27-10-2025', 'dd-mm-yyyy'), '09:00', '10:00', 'Pendiente', 'Chequeo general');
insert into CITA (id_cita, id_paciente, id_clinica, fecha_cita, hora_inicio, hora_fin, estado, descripcion)
values (1762747319573, 1761505535558, 1761505557966, to_date('16-11-2025', 'dd-mm-yyyy'), '10:00', '11:00', 'PENDIENTE', 'aaa');
commit;
prompt 2 records loaded
prompt Enabling foreign key constraints for CITA...
alter table CITA enable constraint FK_CITA_CLINICA;
alter table CITA enable constraint FK_CITA_PACIENTE;
prompt Enabling triggers for CLINICA...
alter table CLINICA enable all triggers;
prompt Enabling triggers for PACIENTE...
alter table PACIENTE enable all triggers;
prompt Enabling triggers for CITA...
alter table CITA enable all triggers;

set feedback on
set define on
prompt Done
