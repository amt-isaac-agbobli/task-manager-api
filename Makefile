build:
	docker-compose -f docker-compose.yaml build

up:
	docker-compose -f docker-compose.yaml up

up-build:
	docker-compose -f docker-compose.yaml up --build

down:
	docker-compose -f docker-compose.yaml down