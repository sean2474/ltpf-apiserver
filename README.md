# adding swag
go install github.com/swaggo/swag/cmd/swag@latest
go get -u github.com/swaggo/swag
export PATH=$(go env GOPATH)/bin:$PATH
swag init
