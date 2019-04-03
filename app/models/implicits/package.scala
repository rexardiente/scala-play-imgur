package models

package object implicits
    extends ImagesJsonFormat
		with    DataJsonFormat
		with 		ImageBase64JsonFormat
		with 		ErrorResponseJsonFormat
		with 		ImageURLsJsonFormat
    with    UploadReadyJsonFormat
		with 		UploadServerReponseJsonFormat
		with 		URLJsonFormat
		with 		SubscribeJsonFormat
		with 		SubmitResponseJsonFormat
