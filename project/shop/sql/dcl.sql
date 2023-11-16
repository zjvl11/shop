-- 프로젝트 전용 계정 생성
create user shop identified by shop;

grant connect, resource, create view to shop;

