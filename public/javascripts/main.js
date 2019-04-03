const PROGRESS_BAR      = document.getElementById("progress-bar-count");
const UPLOADED_IMAGES   = document.getElementById("uploaded-images");
const NOTIFICATION      = document.getElementById("notification-alert");
const IMAGE_LIST        = document.getElementById("image-list");
const CLIENT_ID         = "7f270860-ed1b-4862-894b-9f057ba9b008";
const AUTHENTICATION_ID = "bab4e72bd156e91";
let image_upload_list   = [];
let ws;


openSocket = () => {
  ws = new WebSocket("ws://localhost:9000/ws");

  ws.onopen = (ev) => {
    open(ev);
  }

  ws.onmessage = (ev) => {
    let data = JSON.parse(ev.data)

    switch (data.code) {
      case 'connected':
        console.log("You are now connected.");
        break;

      case 'image_uploaded':
        let link = data.data.data.link;

        updateUploadImages(link);
        break;

      case 'upload_ready':
        let passed = data.passed;
        let failed = data.failed;

        if (passed != 0) uploadImage(passed, data.job_ID);
        if (failed != 0) failedImages(failed);
        break;

      default:
        console.log(data);
    }
  }

  ws.onclose = (ev) => {
    console.log(ev);
  }
}

getImages = () => {
  let req = { "url": "http://localhost:9000/v1/images", "method": "GET" };

  $.ajax(req).done((res) => {
    if (res.uploaded != 0) {
      res.uploaded.forEach((v, i) => updateUploadImages(v));
    }
  });
}

updateUploadImages = (res) => {
  let node = `
    <div class="row" id="image-${res}">
      <div class="col-sm-12">
        <div class="row">
          ${res}
        </div>
      </div>
    </div>`;

  UPLOADED_IMAGES.innerHTML += node;
}

uploadImage = (data, id) => {
  let arr    = data;
  let offset = 0;

  // Create new document elements.
  arr.map((v, i) => {
    let node = `
      <div class="row" id="image-${v.name}">
        <div class="col-sm-10">
          <div class="progress">
            <div class="progress-bar
                progress-bar-striped
                progress-bar-animated"
                role="progressbar"
                aria-valuenow="100"
                aria-valuemin="0"
                aria-valuemax="100"
                style="width: 100%">
            </div>
          </div>
        </div>
        <div class="col-sm-2" id="icon-status">
          <span class="oi oi-data-transfer-upload"></span>
        </div>
        <br/>
      </div>`;

    PROGRESS_BAR.innerHTML += node;
    return v;
  }).map((v, i) => {
    // Set timeout interval each request.
    let name         = v.name;
    let req          = uploadSettingIMGUR(name, v.value);
    let elem_name    = `image-${name}`;
    let per_progress = document.getElementById(elem_name);

    setTimeout(() => {
      $.ajax(req).done((res) => {
        $(per_progress).find(".progress-bar").addClass("bg-success");

        setTimeout(() => {
          uploadResponseIMGUR(id, res);
          PROGRESS_BAR.removeChild(per_progress);
        }, 1000);
      });
    }, 1000 + offset);

    offset += 1000;
  });
}

uploadSettingIMGUR = (FILE_NAME, URL) =>  {
  let form = new FormData();

  form.append("image", URL);
  form.append("type", "base64");
  form.append("name", FILE_NAME);

  let settings = {
    "url": "https://api.imgur.com/3/image",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Authorization": `Client-ID ${ AUTHENTICATION_ID }`
    },
    "crossDomain": true,
    "processData": false,
    "mimeType": "multipart/form-data",
    "contentType": false,
    "data": form
  };

  return settings;
}

base64ToImage = (str) => {
  let image = new Image();
  image.src = `data:image/${ str.image_type };base64,${ str.value }`;

  return image;
}

uploadResponseIMGUR = (id, res) => {
  let message       = JSON.parse(res);
  message.client_ID = CLIENT_ID;
  message.job_ID    = id;

  if (ws.readyState === 1)
    ws.send(JSON.stringify(message));
  else
    console.log("Unable to send.");
}

open = (ev) => {
  let message = {
    "client_ID": CLIENT_ID,
    "command": "subscribe"
  }

  if (ws.readyState === 1)
    ws.send(JSON.stringify(message));
  else
    console.log("Unable to send.");
}

failedImages = (res) => {
  res.forEach((v, i) => {
    let node = `
      <div class="alert alert-warning alert-dismissible fade show" role="alert">
        Image <strong> ${v.name} </strong> Already Exists.
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>`;

    NOTIFICATION.innerHTML += node;
  });

  alertWarningClose();
}

$("#btn-add-to-list").click((ev) => {
  let elem_ID = document.getElementById("inputImageURL");
  let url     = elem_ID.value;

  // Do nothing if there is no value inside the text field.
  if (url.length != 0) {
    if (image_upload_list.includes(url))
      failedImages(new Array({"name": url}))
    else {
      image_upload_list.push(url);

      IMAGE_LIST.innerHTML = "";

      // Update URL list display.
      image_upload_list.forEach((v, i) => {
        IMAGE_LIST.innerHTML += `<p>${image_upload_list[i]}</p>`;
      });
    }
  }

  // reset text field value to null after adding to list.
  elem_ID.value = null;
});


$("#btn-upload-list").click((ev) => {
  let form = new FormData();

  form.append("client_ID", CLIENT_ID);
  form.append("urls", image_upload_list);

  let req = {
    "url": "http://localhost:9000/v1/images/upload",
    "method": "POST",
    "timeout": 0,
    "headers": {},
    "crossDomain": true,
    "processData": false,
    "contentType": false,
    "data": form
  };

  $.ajax(req).done((res) => {
    let node = `
      <div class="alert alert-success alert-dismissible fade show" role="alert">
        Image Job ID: <strong> ${res.job_ID} </strong>.
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>`;

    NOTIFICATION.innerHTML += node;
    alertSuccessClose();
  });

  // reset to default.
  image_upload_list    = [];
  IMAGE_LIST.innerHTML = "";
});

// preventing Enter key to submit.
$(document).keypress((ev) => {
  if (event.which == '13') {
    event.preventDefault();
  }
});

alertSuccessClose = () => {
  $(".alert-success").fadeTo(3000, 500).slideUp(500, () => {
    $(".alert-success").slideUp(500);
    removeNotification();
  });
}

alertWarningClose = () => {
  $(".alert-warning").fadeTo(3000, 500).slideUp(500, () => {
    $(".alert-warning").slideUp(500);
    removeNotification();
  });
}

removeNotification = () => {
  NOTIFICATION.innerHTML = "";
}

// WebSocket modules.
openSocket();
getImages();

