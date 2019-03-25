package models

package object implicits 
		extends ClientJsonFormat
		with 		DownloadJsonFormat
		with 		ErrorResponseJsonFormat
		with 		ImageURLsJsonFormat
		with 		UploadReadyJsonFormat
		with 		URLJsonFormat
		with 		SubscribeJsonFormat
		with 		SubmitResponseJsonFormat
