function isEmpty(str) {
  return !str || 0 === str.length;
}

function isBlank(str) {
  return !str || /^\s*$/.test(str);
}

function prefixInteger(num, length) {
  return (Array(length).join("0") + (isEmpty(num) ? 0 : num)).slice(-length);
}

function removeThousandSeparator(price) {
  price = price.replace(/,/g, "");
  const val = parseInt(price);
  return val;
}

function addThousandSeparator(price) {
  price = price.toString().replace(/\,/g, "");
  price = changeNumber(price);
  let re = /(-?\d+)(\d{3})/;
  while (re.test(price)) {
    price = price.replace(re, "$1,$2");
  }

  return price;
}

function changeNumber(x) {
  x = (~~x).toString();
  return x;
}

function isNumber(nubmer) {
  let re = /^[-]?\d+$/;
  return re.test(nubmer);
}

function isPositiveNumber(nubmer) {
  let re = /^[1-9]\d*$/;
  return re.test(nubmer);
}

function showError(error) {
  // console.log(error);
  $.each(error, function (k, v) {
    let msg = "";

    for (var i = 0; i < v.length; i++) {
      if (i > 0) {
        msg = msg + "<br/>";
      }

      msg += v[i];
    }

    let elm = $("<span>").prop("class", "valid-err text-danger").html(msg);

    $("#" + k).after(elm);
  });
}

function genRandomArbitrary(min, max) {
  return Math.random() * (max - min) + min;
}

function genRandom(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function isNull(arg) {
  if (arg === null || arg === "" || arg === undefined) {
    return true;
  }

  return false;
}

function callAjax(url, obj, fn) {
  if (event) {
    event.preventDefault();
  }

  obj = obj || $("form").serialize();

  $.ajax({
    type: "POST",
    url: url,
    data: obj,
    beforeSend: function () {
      openLoading();
    },
    complete: function () {
      closeLoading();
    },
    success: function (data) {
      if (data.code === "998") {
        showError(data.errorMessages);
      } else if (data.code === "000") {
        messagePopup(data.message);

        if (fn) {
          fn();
        }
      } else if (fn) {
        fn(data);
      }
    },
    error: function (e) {
      var errorMessages = e.responseJSON.errorMessages;
      var errorMessagesStr = "";
      for (var m in errorMessages) {
        for (var i = 0; i < errorMessages[m].length; i++) {
          errorMessagesStr = errorMessagesStr + errorMessages[m][i];
        }
      }
      $.confirm({
        title: e.responseJSON.message,
        titleClass: "text-center d-block font-weight-bold text-warning2",
        content: errorMessagesStr,
        buttons: {
          確定: {
            text: "確定",
            btnClass: "btn-warning2 text-white",
          },
        },
      });
    },
  });
}

function messagePopup(msg) {
  $("#messageModal").find(".modal-body").text(msg);
  $("#messageModal").modal("show");
}

function errorMessagePopup(msg) {
  $("#errorMessageModal").find(".modal-body").text(msg);
  $("#errorMessageModal").modal("show");
}

function refreshPage() {
  $("#messageModal").on("hidden.bs.modal", function () {
    $(this).find(".modal-body").empty();

    location.reload();
  });
}

function gotoPage(url) {
  if (event) {
    event.preventDefault();
  }

  location.href = url;
}

function callPost(id, url) {
  let form = document.createElement("form");
  form.method = "post";
  form.action = url;
  form.style.display = "hidden";

  let myInput = document.createElement("input");
  myInput.setAttribute("name", "id");
  myInput.setAttribute("value", id);

  form.appendChild(myInput);
  document.body.appendChild(form);
  form.submit();
  document.body.removeChild(form);
}

function formSubmit(url, params, target) {
  let form = document.createElement("form");
  form.method = "post";
  form.action = url;
  form.style.display = "hidden";
  if (target) {
    form.target = target;
  }

  for (let key in params) {
    if (params.hasOwnProperty(key)) {
      let input = document.createElement("input");
      input.setAttribute("name", key);
      input.setAttribute("value", params[key]);
      form.appendChild(input);
    }
  }

  document.body.appendChild(form);
  form.submit();
  document.body.removeChild(form);
}

function genUUID() {
  let d = Date.now();
  if (
    typeof performance !== "undefined" &&
    typeof performance.now === "function"
  ) {
    d += performance.now(); // use high-precision timer if available
  }
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    let r = (d + Math.random() * 16) % 16 | 0;
    d = Math.floor(d / 16);
    return (c === "x" ? r : (r & 0x3) | 0x8).toString(16);
  });
}

function getWatermarkInit(str) {
  watermark.init({
    watermark_txt: str,
  });

  watermark.load({
    watermark_x: 150,
    watermark_y: 150,
    watermark_rows: 5,
    watermark_cols: 16,
    watermark_x_space: 57,
    watermark_y_space: 55,
    watermark_alpha: 0.06,
    watermark_width: 300,
    watermark_height: 74,
    watermark_angle: 15,
    watermark_fontsize: "22px",
  });
}

function startEndCalendarInit(objStart, objEnd) {
  var d1 = new Date(objStart.val());
  var d2 = new Date();
  var d3 = new Date(objStart.val());
  var d4 = new Date();

  d1.setDate(d1.getDate() + 1);
  d2.setDate(1);
  d3.setDate(1);
  d4.setDate(1);
  d4.setHours(8, 0, 0, 0);

  d1 = FormatDate(d1);
  d2 = FormatDate(d2);

  if (d3.getTime() >= d4.getTime()) {
    objStart.attr("min", d2);
  }

  objEnd.attr("min", d1);
}

function startEndCalendarChange(objStart, objEnd) {
  var d1 = new Date(objStart.val());
  var d2 = new Date(objStart.val());
  var d3 = new Date(objEnd.val());

  d1.setDate(d1.getDate() + 1);
  d1 = FormatDate(d1);

  objEnd.attr("min", d1);

  if (d2.getTime() >= d3.getTime()) {
    objEnd.val(d1);
  }
}

function minNewDate(objStart) {
  var d1 = new Date();
  d1 = FormatDate(d1);
  objStart.attr("min", d1);
}

function minNewDate_1(objStart) {
  var d1 = new Date();
  d1.setDate(d1.getDate() + 1);
  d1 = FormatDate(d1);
  objStart.attr("min", d1);
}

function FormatDate(strTime) {
  var date = new Date(strTime);
  var formatedMonth = ("0" + (date.getMonth() + 1)).slice(-2);
  var formatedDate = ("0" + date.getDate()).slice(-2);
  return date.getFullYear() + "-" + formatedMonth + "-" + formatedDate;
}

function ckdate(obj1, obj2) {
  var starttime = obj1.val();
  var endtime = obj2.val();

  var start = new Date(starttime.replace("-", "/").replace("-", "/"));
  var end = new Date(endtime.replace("-", "/").replace("-", "/"));

  if (end <= start) {
    alert("結束日期不能小於等於開始日期");
    return false;
  } else {
    return true;
  }
}

function isPoneAvailable(pone) {
  var myreg = /^09\d{8}$/;
  if (!myreg.test(pone)) {
    return false;
  } else {
    return true;
  }
}

function getRatio() {
  var viewportHeight =
    document.documentElement.clientHeight || document.body.clientHeight;
  var viewportWidth =
    document.documentElement.clientWidth || document.body.clientWidth;
  var designHeight = 1080;
  var designWidth = 1920;
  var zoomValueHeight = viewportHeight / designHeight;
  var zoomValueWidth = viewportWidth / designWidth;
  return zoomValueWidth > zoomValueHeight ? zoomValueWidth : zoomValueHeight;
}

function isScale() {
  var rate = detectZoom();
  if (rate != 100) {
    console.log(1);
  }
}

function detectZoom() {
  var ratio = 0;
  var screen = window.screen;
  var ua = navigator.userAgent.toLowerCase();

  if (window.devicePixelRatio !== undefined) {
    ratio = window.devicePixelRatio;
  } else if (~ua.indexOf("msie")) {
    if (screen.deviceXDPI && screen.logicalXDPI) {
      ratio = screen.deviceXDPI / screen.logicalXDPI;
    }
  } else if (
    window.outerWidth !== undefined &&
    window.innerWidth !== undefined
  ) {
    ratio = window.outerWidth / window.innerWidth;
  }

  if (ratio) {
    ratio = Math.round(ratio * 100);
  }

  return ratio;
}

function getZoom() {
  const width = window.innerWidth;
  const pixelRatio = window.devicePixelRatio || 1;
  const scaleWidth = width * pixelRatio;

  let minZoom = 0.4;
  let maxZoom = 1;

  if (scaleWidth >= 800 && scaleWidth <= 1440) {
    let zoomValue =
      minZoom + ((scaleWidth - 800) * (maxZoom - minZoom)) / (1920 - 800);
    return zoomValue;
  } else if (scaleWidth < 800) {
    return minZoom;
  } else {
    return 0.7;
  }
}

function validateNumberInput(event) {
  const inputValue = event.target.value;
  const regex = /^(0|[1-9][0-9]*)$/;

  if (!regex.test(inputValue) && inputValue !== "") {
    event.target.value = inputValue.slice(0, -1);
  }
}
