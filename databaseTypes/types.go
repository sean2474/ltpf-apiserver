package databasetypes

type User struct {
	ID        int64  `json:"id"`
	FirstName string `json:"first_name"`
	LastName  string `json:"last_name"`
	Email 	  string `json:"email"`
	Password  string `json:"password"`
	Phone 	  string `json:"phone"`
	Address   string `json:"address"`
	IsAdmin   bool   `json:"is_admin"`
}

type Item struct {
	ID          int64    `json:"id"`
	SubmitterID int64    `json:"submitter_id"`
	Address		  string   `json:"address"`
	ItemType 	  string   `json:"item_type"`
	Description string   `json:"description"`
	ImageURLs   []string `json:"image_urls"`
	ExtraInfo	  string   `json:"extra_info"` // purpose, etc.
}

type Contact struct {
	ID        int64  `json:"id"`
	Title 	  string `json:"title"`
	FirstName string `json:"first_name"`
	LastName  string `json:"last_name"`
	Content   string `json:"content"`
	Email 	  string `json:"email"`
	Phone 	  string `json:"phone"`
}