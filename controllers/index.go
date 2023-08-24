package controllers

import (
	"encoding/json"
	"net/http"
)

func writeJson(w http.ResponseWriter, resp interface{}) {
	jsonResp, err := json.Marshal(resp)
	if err != nil {
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	// Send response
	w.Header().Set("Content-Type", "application/json; charset=UTF-8")
	w.WriteHeader(http.StatusOK)
	w.Write(jsonResp)
}

func UserHandler(w http.ResponseWriter, r *http.Request) {
	writeJson(w, "User page")
}

func ContactHandler(w http.ResponseWriter, r *http.Request) {
	writeJson(w, "Contact page")
}

func ItemHandler(w http.ResponseWriter, r *http.Request) {
	writeJson(w, "Item page")
}