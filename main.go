package main

import (
	"ltpf-apiserver/controllers"
	_ "ltpf-apiserver/docs"
	"net/http"

	"github.com/rs/cors"
	httpSwagger "github.com/swaggo/http-swagger"
)

// @title Go Rest API with Swagger for Level The Playing Field project
// @version 1.0
// @description Simple swagger implementation in Go HTTP
// @contact.name Sean
// @BasePath /
// @securityDefinitions.apikey Bearer
// @in header
// @name Authorization
// @description Type "Bearer" followed by a space and JWT token.
func main() {
	// Create a new cors handler with permissive options (allowing all origins)
	corsHandler := cors.New(cors.Options{
		AllowedOrigins:   []string{"*"}, // Allow all origins, you can restrict this to specific origins if needed
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Authorization", "Content-Type"},
		AllowCredentials: true,
	})

	// Apply the cors handler to your existing handlers
	http.Handle("/swagger/", corsHandler.Handler(httpSwagger.WrapHandler))
	http.Handle("/contact", corsHandler.Handler(http.HandlerFunc(controllers.ContactHandler)))
	http.Handle("/item", corsHandler.Handler(http.HandlerFunc(controllers.ItemHandler)))

	// Start the server with your handlers
	http.ListenAndServe(":8082", nil)
}
