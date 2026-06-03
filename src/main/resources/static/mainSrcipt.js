// =============================================================================
// ПОШУК
// =============================================================================

// students.html (admin) + teacherStudents/students.html (teacher) — ідентичні
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchStudent() {
    var input, filter, a, i, txtValue;
    input = document.getElementById('myInput');
    student = document.getElementById('students').getElementsByTagName("tr");
    filter = input.value.toUpperCase();
    for (i = 0; i < student.length; i++) {
        a = student[i].getElementsByTagName("td")[1];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            student[i].style.display = "";
        } else {
            student[i].style.display = "none";
        }
    }
}

// teachers.html (admin)
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchTeacher() {
    var input, filter, a, i, txtValue;
    input = document.getElementById('myInput');
    teachers = document.getElementById('teachers').getElementsByTagName("tr");
    filter = input.value.toUpperCase();
    for (i = 0; i < teachers.length; i++) {
        a = teachers[i].getElementsByTagName("td")[1];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            teachers[i].style.display = "";
        } else {
            teachers[i].style.display = "none";
        }
    }
}

// teacherInfo.html (admin) — пошук по label+input парах
// teacherLessons.html (admin) + teacherLessons/lessons.html (teacher) — ідентичні
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchStudents() {
    var input, filter, a, b, i, txtValue;
    input = document.getElementById('myInputStudents');
    let studentIds = document.getElementById('studentIds').getElementsByTagName("input");
    let studentNames = document.getElementById('studentNames').getElementsByTagName("label");
    filter = input.value.toUpperCase();
    for (i = 0; i < studentNames.length; i++) {
        a = studentNames[i];
        b = studentIds[i];
        txtValue = a.textContent.split(" ")[0] || a.innerText.split(" ")[0];
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            studentNames[i].style.display = "";
            studentIds[i].style.display = "";
        } else {
            studentNames[i].style.display = "none";
            studentIds[i].style.display = "none";
        }
    }
}

// Використовується тільки в teacherInfo.html (admin) — шукає без split(" ")[0]
// ПЕРЕЙМЕНОВАНО: searchStudentsInTeacherInfo (оригінал — searchStudents, але логіка без split)
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchStudentsInTeacherInfo() {
    var input, filter, a, b, i, txtValue;
    input = document.getElementById('myInputStudents');
    let studentIds = document.getElementById('studentIds').getElementsByTagName("input");
    let studentNames = document.getElementById('studentNames').getElementsByTagName("label");
    filter = input.value.toUpperCase();
    for (i = 0; i < studentNames.length; i++) {
        a = studentNames[i];
        b = studentIds[i];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            studentNames[i].style.display = "";
            studentIds[i].style.display = "";
        } else {
            studentNames[i].style.display = "none";
            studentIds[i].style.display = "none";
        }
    }
}

// editTeacher.html (admin), teacherStudentLessons.html (admin),
// teacherLessons.html (admin+teacher), teacherLessons/lessons.html (teacher) — ідентичні (courseSelect → option)
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchCourse() {
    var input, filter, a, b, i, txtValue;
    input = document.getElementById('myInputCourses');
    let courseIds = document.getElementById('courseIds').getElementsByTagName("input");
    let courseNames = document.getElementById('courseNames').getElementsByTagName("label");
    filter = input.value.toUpperCase();
    for (i = 0; i < courseNames.length; i++) {
        a = courseNames[i];
        b = courseIds[i];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            courseNames[i].style.display = "";
            courseIds[i].style.display = "";
        } else {
            courseNames[i].style.display = "none";
            courseIds[i].style.display = "none";
        }
    }
}

// editTeacher.html (admin) + teacherHomepage (teacher) — пошук по courseSelect > option
// ПЕРЕЙМЕНОВАНО: searchCourseSelect (оригінал — searchCourse, але шукає в <select> а не в courseIds/courseNames)
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchCourseSelect() {
    var input, filter, a, i, txtValue;
    input = document.getElementById('myInput');
    courses = document.getElementById('courseSelect').getElementsByTagName("option");
    filter = input.value.toUpperCase();
    for (i = 0; i < courses.length; i++) {
        a = courses[i];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            courses[i].style.display = "";
        } else {
            courses[i].style.display = "none";
        }
    }
}

// addNewTeacher.html (admin) + signUpAsTeacher.html — пошук по div > span > span > span
// ПЕРЕЙМЕНОВАНО: searchCourseInDivs (оригінал — searchCourse, але шукає в <div> структурі)
// Функція фільтрує список на сторінці за введеним текстом користувача.
// Вона працює напряму з DOM-елементами, тому одна й та сама логіка використовується у кількох шаблонах.
function searchCourseInDivs() {
    var input, filter, a, i, txtValue;
    input = document.getElementById('myInput');
    courses = document.getElementById('courses').getElementsByTagName("div");
    filter = input.value.toUpperCase();
    for (i = 0; i < courses.length; i++) {
        a = courses[i].getElementsByTagName("span")[0].getElementsByTagName("span")[0].getElementsByTagName("span")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            courses[i].style.display = "";
        } else {
            courses[i].style.display = "none";
        }
    }
}


// =============================================================================
// ПАРОЛІ
// =============================================================================

// addNewStudent.html (admin) + addNewTeacher.html (admin) — ідентичні
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function managePassNew() {
    // let pass = document.querySelector("#pass")
    // let form = document.querySelector("#form")
    // if (pass.value === "") {
    //     form.action = form.action + "NOPASS"
    // } else {
    //     form.action = form.action + "" + pass.value
    // }
    let pass = document.querySelector("#pass")
    if (pass.value === "") {
        pass.value =  "NO_PASS";
    }
    console.log(pass.value)
}

function manageButtonWithoutFormInAdmin(el){
    document.querySelector("#form").action = "/admin/teacher/"+el.dataset.tid+"/deleteCourse/"+el.dataset.cid;

}
function manageButtonWithoutFormInTeacher(el){
    document.querySelector("#form").action = "/teacher/"+el.dataset.tid+"/deleteCourse/"+el.dataset.cid;

}
// editStudent.html (admin)
// ПЕРЕЙМЕНОВАНО: managePassEditStudent (оригінал — parsePass)
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function managePassEditStudent() {
    // let id = document.getElementById("id").innerText
    // let pass = document.querySelector("#pass")
    // if (pass.value === "") {
    //     form.action = "/admin/student/" + id + "/edit/OLDPASS"
    // } else {
    //     form.action = "/admin/student/" + id + "/edit/" + pass.value
    // }
    let pass = document.querySelector("#pass")
    if (pass.value === "") {
        pass.value =  "OLD_PASS";
    }
}

// editTeacher.html (admin)
// ПЕРЕЙМЕНОВАНО: managePassEditTeacher (оригінал — managePass, але URL відрізняється)
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function managePassEditTeacher() {
    // let id = document.getElementById("id").innerText
    // let pass = document.querySelector("#pass")
    // let form = document.querySelector("#form")
    // if (pass.value === "") {
    //     form.action = "/admin/teacher/" + id + "/edit/OLDPASS"
    // } else {
    //     form.action = "/admin/teacher/" + id + "/edit/" + pass.value
    // }
    let pass = document.querySelector("#pass")
    if (pass.value === "") {
        pass.value =  "OLD_PASS";
    }
}

// teacherHomepage/homepage.html (teacher)
// ПЕРЕЙМЕНОВАНО: managePassTeacher (оригінал — managePass, але URL /teacher/...)
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function managePassTeacher() {
    // let id = document.getElementById("id").innerText
    // let pass = document.querySelector("#pass")
    // let form = document.querySelector("#form")
    // if (pass.value === "") {
    //     form.action = "/teacher/" + id + "/homepage/OLDPASS"
    // } else {
    //     form.action = "/teacher/" + id + "/homepage/" + pass.value
    // }
    let pass = document.querySelector("#pass")
    if (pass.value === "") {
        pass.value =  "OLD_PASS";
    }
}

// studentHomepage/homepage.html (student)
// ПЕРЕЙМЕНОВАНО: managePassStudent (оригінал — managePass, але читає #idSt і URL /student/...)
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function managePassStudent() {
    // let id = document.getElementById("idSt")
    // let pass = document.querySelector("#pass")
    // let form = document.querySelector("#form")
    // if (pass.value === "") {
    //     form.action = "/student/" + id.innerText + "/homepage/OLDPASS"
    // } else {
    //     form.action = "/student/" + id.innerText + "/homepage/" + pass.value
    // }
    let pass = document.querySelector("#pass")
    if (pass.value === "") {
        pass.value =  "OLD_PASS";
    }
}

// signUpAsStudent.html + signUpAsTeacher.html (openSource)
// ПЕРЕЙМЕНОВАНО: managePassSignUp (оригінал — managePass, але логіка: встановлює pass.value="NOPASS" і дописує в action)
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function managePassSignUp() {
    // let pass = document.querySelector("#pass")
    // let form = document.querySelector("#form")
    // if (pass.value === "") {
    //     pass.value = "NOPASS"
    // }
    // form.action = form.action + "/" + pass.value
    let pass = document.querySelector("#pass")
    if (pass.value === "") {
        pass.value =  "NO_PASS";
    }
}


// =============================================================================
// ВІЛЬНИЙ ЧАС (ЧЕКБОКСИ) — freeTimeCheckbox
// editTeacher.html (admin), teacherInfo.html (admin),
// teacherHomepage/homepage.html (teacher), addNewTeacher.html (admin)
// =============================================================================

let freeTimeChanged = false;
let isMouseDown = false;

document.body.onmousedown = () => (isMouseDown = true);
document.body.onmouseup = () => (isMouseDown = false);

document.querySelectorAll('.freeTimeCheckbox').forEach(checkbox => {
    checkbox.addEventListener('mouseover', () => {
        if (isMouseDown) {
            checkbox.checked = !checkbox.checked;
        }
    });
});

document.querySelectorAll('.freeTimeCheckbox').forEach(cb => {
    cb.addEventListener('change', () => {
        freeTimeChanged = true;
    });
});

// editTeacher.html (admin), teacherInfo.html (admin), teacherHomepage (teacher) — ідентичні
// Функція перевіряє дію перед відправкою форми та показує підтвердження користувачу.
// Це захищає від випадкових змін, які можуть вплинути на розклад або особисті дані.
function handleSubmit(event) {
    if (freeTimeChanged) {
        return confirm("Якщо ви змінете свої вільні часи то ваші заняття з учнями на ці часи будуть видалені. Продовжити?");
    }
    return true;
}

// Додає можливість виділяти весь день кліком на заголовок таблиці
// Допоміжна функція приводить дату або день тижня до формату, зручного для інтерфейсу.
// Її результат використовується у попапах та підписах розкладу.
(function initDaySelection() {
    document.querySelectorAll('.times-table').forEach(table => {
        const headers = table.querySelectorAll('thead th');
        headers.forEach((th, index) => {
            if (index === 0) return; // Пропускаємо колонку "Година"
            
            th.style.cursor = 'pointer';
            th.title = 'Натисніть, щоб вибрати/скасувати всі в цей день';
            
            th.addEventListener('click', () => {
                const checkboxes = table.querySelectorAll(`tbody tr td:nth-child(${index + 1}) .freeTimeCheckbox`);
                if (checkboxes.length === 0) return;
                
                // Якщо хоча б один не вибраний — вибираємо всі. Інакше — знімаємо всі.
                const anyUnchecked = Array.from(checkboxes).some(cb => !cb.checked);
                checkboxes.forEach(cb => {
                    cb.checked = anyUnchecked;
                    cb.dispatchEvent(new Event('change'));
                });
            });
        });
    });
})();

// studentHomepage/homepage.html (student) — інший текст підтвердження
// ПЕРЕЙМЕНОВАНО: handleSubmitStudent (оригінал — handleSubmit)
// Функція перевіряє дію перед відправкою форми та показує підтвердження користувачу.
// Це захищає від випадкових змін, які можуть вплинути на розклад або особисті дані.
function handleSubmitStudent(event) {
    return confirm("Змінити данні?");
}

// editTeacher.html (admin), teacherInfo.html (admin), teacherHomepage (teacher),
// studentHomepage (student) — ідентичні
// Функція готує пароль перед відправкою форми на сервер.
// Якщо поле порожнє, у URL або form action передається службове значення, яке backend розуміє як старий або відсутній пароль.
function togglePassword() {
    const input = document.getElementById("passwordField");
    const btn = event.target;
    if (input.type === "password") {
        input.type = "text";
        btn.innerText = "Сховати";
    } else {
        input.type = "password";
        btn.innerText = "Показати";
    }
}


// =============================================================================
// ДОДАВАННЯ КУРСІВ У СПИСОК (SELECT → LIST)
// editTeacher.html (admin) + teacherHomepage/homepage.html (teacher) — ідентичні
// =============================================================================

// Ініціалізатор підключає обробники подій після завантаження DOM-елементів.
// Такий підхід дозволяє одному JS-файлу працювати на різних сторінках, де частина елементів може бути відсутня.
(function initCourseSelect() {
    const select = document.getElementById("courseSelect");
    const list = document.getElementById("coursesList");
    const inputsDiv = document.getElementById("coursesInputs");
    if (!select || !list || !inputsDiv) return;

    let addedCourses = new Set();

    select.addEventListener("change", function () {
        const selectedOption = select.options[select.selectedIndex];
        const courseId = selectedOption.value;
        const courseName = selectedOption.text;

        if (!courseId) return;

        let present = false;
        for (let i = 0; i < list.getElementsByTagName("li").length; i++) {
            if (list.getElementsByTagName("li")[i].innerText == courseId) {
                present = true;
            }
        }

        if (addedCourses.has(courseId) || present) {
            alert("Цей курс вже доданий");
            return;
        }

        const li = document.createElement("li");
        li.innerText = courseName;
        list.appendChild(li);
        const liId = document.createElement("li");
        liId.innerText = courseId;
        liId.style.display = "none";
        list.appendChild(liId);

        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "newCourseIds";
        input.value = courseId;
        inputsDiv.appendChild(input);

        addedCourses.add(courseId);
        select.value = "";
    });
})();


// =============================================================================
// ЗВ'ЯЗОК СТУДЕНТ → ПОСИЛАННЯ (teacherInfo.html admin)
// =============================================================================

// Ініціалізатор підключає обробники подій після завантаження DOM-елементів.
// Такий підхід дозволяє одному JS-файлу працювати на різних сторінках, де частина елементів може бути відсутня.
(function initTeacherStudentLink() {
    const teacherIdEl = document.querySelector("#teacherId");
    const link = document.querySelector("#link");
    const students = document.querySelector("#studentIds");
    if (!teacherIdEl || !link || !students) return;

    let teachId = teacherIdEl.innerText;
    let stIds = students.getElementsByTagName("input");

    for (let i = 0; i < stIds.length; i++) {
        stIds[i].addEventListener('change', () => {
            if (stIds[i].checked) {
                link.href = `/admin/teacher/` + teachId + `/lessonsWithStudent/` + stIds[i].value;
            }
        });
    }
})();


// =============================================================================
// СКОРОЧЕННЯ ТЕКСТУ (openSourceHomepage/homepage.html)
// =============================================================================

// Функція скорочує довгі описи до компактного вигляду на публічних сторінках.
// Повний текст зберігається в data-атрибуті, а користувач бачить коротку превю-версію.
(function truncateDescriptions() {
    const maxWords = 10;
    document.querySelectorAll('.course-description, .teacher-description').forEach(function (element) {
        const fullText = (element.dataset.fullText || '').trim();
        const words = fullText.split(/\s+/).filter(Boolean);
        if (words.length > maxWords) {
            element.textContent = words.slice(0, maxWords).join(' ') + '...';
        } else {
            element.textContent = fullText;
        }
    });
})();

// Функція позначає останню клітинку тривалого уроку у таблиці розкладу.
// Це допомагає CSS правильно намалювати візуальний блок заняття, яке займає кілька часових слотів.
(function markLessonSegmentEnds() {
    document.querySelectorAll(".lesson-table-wrapper table").forEach(table => {
        const rows = Array.from(table.querySelectorAll("tbody tr"));
        rows.forEach(row => {
            row.querySelectorAll(".lesson-segment-start, .lesson-segment-end").forEach(button => {
                button.classList.remove("lesson-segment-start", "lesson-segment-end");
            });
            row.querySelectorAll(".lesson-segment-cell, .lesson-segment-cell-end").forEach(cell => {
                cell.classList.remove("lesson-segment-cell", "lesson-segment-cell-end");
            });
            row.querySelectorAll(".lesson-segment-cell").forEach(cell => {
                cell.style.opacity = "0 !important";
            });

        });

        rows.forEach((row, rowIndex) => {
            const cells = row.children;
            for (let columnIndex = 1; columnIndex < cells.length; columnIndex++) {
                const cell = cells[columnIndex];
                const button = cell.querySelector(".form-btn");
                if (!button) continue;

                const previousRow = rows[rowIndex - 1];
                const previousCell = previousRow ? previousRow.children[columnIndex] : null;
                const previousButton = previousCell ? previousCell.querySelector(".form-btn") : null;
                const nextRow = rows[rowIndex + 1];
                const nextCell = nextRow ? nextRow.children[columnIndex] : null;
                const nextButton = nextCell ? nextCell.querySelector(".form-btn") : null;
                const previousLessonId = previousButton ? previousButton.dataset.id : null;
                const currentLessonId = button.dataset.id;
                const nextLessonId = nextButton ? nextButton.dataset.id : null;
                const lessonStarts = previousLessonId !== currentLessonId;
                const lessonContinues = nextLessonId === currentLessonId;

                cell.classList.add("lesson-segment-cell");
                if (lessonStarts) {
                    button.classList.add("lesson-segment-start");
                }
                if (!lessonContinues) {
                    button.classList.add("lesson-segment-end");
                    cell.classList.add("lesson-segment-cell-end");
                }
            }
        });
    });
})();


// =============================================================================
// POPUP ДЛЯ УРОКІВ
// =============================================================================

// Допоміжна: рендеринг назви дня тижня
// Допоміжна функція приводить дату або день тижня до формату, зручного для інтерфейсу.
// Її результат використовується у попапах та підписах розкладу.
function getDayName(dayVal) {
    const days = {1: "Понеділок", 2: "Вівторок", 3: "Середа", 4: "Четвер", 5: "Пятниця", 6: "Субота", 7: "Неділя"};
    return days[dayVal] || "";
}

// Допоміжна: форматування дати з ISO-рядка
// Допоміжна функція приводить дату або день тижня до формату, зручного для інтерфейсу.
// Її результат використовується у попапах та підписах розкладу.
function formatDateFromKey(element) {
    const [datePart, timePart] = element.split("T");
    const [y, m, d] = datePart.split("-");
    const minutes = timePart.split(":")[1];
    console.log(`DayOfWeek ${d}.${m} ${timePart.split(":")[0]}:${timePart.split(":")[1]}`)
    return { display: `${d}.${m}`, isoDate: `DayOfWeek ${d}.${m} ${timePart.split(":")[0]}:${timePart.split(":")[1]}`, minutes };
}

// Функція керує попапами уроків: відкриває їх, заповнює даними або закриває.
// Вона читає dataset з клітинок розкладу та підставляє потрібні значення у форми.
function initLessonPopup(config) {
    const popup = document.querySelector("#lessonPopup");
    const popupDate = document.querySelector("#popupDate");
    const popupTime = document.querySelector("#popupTime");
    const popupDeleteBtn = document.querySelector("#deleteLessonBtn");
    const popupDeleteCourseBtn = document.querySelector("#deleteCourseBtn");
    const popupEditBtn = document.querySelector("#editLessonBtn");
    const durInMain = document.querySelector("#durInMain");
    const durInEdit = document.querySelector("#durInEdit");
    const durInPast = document.querySelector("#durInPast");
    const cost = document.querySelector("#lessonCost");
    const popupLessonID = document.querySelector("#lessonId");
    const popupCourseName = document.querySelector("#courseName");
    const popupStudentName = document.querySelector("#studentName");
    const popupTeacherName = document.querySelector("#teacherName");
    const popupMain = document.querySelector("#popupMain");
    const popupEdit = document.querySelector("#editPopup");
    const popupEditDates = document.querySelector("#popupDateInEditIdsReturn");
    const returnIdValue = document.querySelector("#returnIdValue");
    const popupDateInEdit = document.querySelector("#dateInEdit");
    const popupTimeInEdit = document.querySelector("#timeInEdit");
    const popupCourseNameInEdit = document.querySelector("#courseNameInEdit");
    const popupStudentNameInEdit = document.querySelector("#studentNameInEdit");
    const popupTeacherNameInEdit = document.querySelector("#teacherNameInEdit");
    const editForm = document.querySelector("#editForm");
    const deleteLessonForm = document.querySelector("#deleteLessonForm");
    const deleteCourseForm = document.querySelector("#deleteCourseForm");
    const popupDateInEmptyPopup = document.querySelector("#popupDateInEmptyPopup");
    const popupTimeInEmptyPopup = document.querySelector("#popupTimeInEmptyPopup");
    const emptyPopupEl = document.querySelector("#emptyPopup");
    const emptyPopupAddLessonForm = document.querySelector("#addLessonForm");
    const emptyPopupAddCourseForm = document.querySelector("#addCourseForm");
    const courseIdInEmptyPopup = document.querySelector("#courseIds");
    const studentIdInEmptyPopup = document.querySelector("#studentIds");
    const durationInEmptyPopup = document.querySelector("#dur");
    const pastLessonPopup = document.querySelector("#pastLessonPopup");
    const popupDateInPastPopup = document.querySelector("#popupDateInPastPopup");
    const popupTimeInPastPopup = document.querySelector("#popupTimeInPastPopup");
    const popupCourseNameInPastPopup = document.querySelector("#courseNameInPastPopup");
    const popupStudentNameInPastPopup = document.querySelector("#studentNameInPastPopup");
    const popupStatusInPastPopup = document.querySelector("#statusInPastPopup");
    const wasForm = document.querySelector("#setWasForm");
    const wasFormInPastPopup = document.querySelector("#setWasFormInPastPopup");
    const stIdInEmptyPopup = document.querySelector("#stIdInEmptyPopup");

    function getIds() {
        return config.getIds ? config.getIds() : {};
    }

    function removeClass(el, cls) { if (el && el.classList.contains(cls)) el.classList.remove(cls); }
    function addClass(el, cls) { if (el && !el.classList.contains(cls)) el.classList.add(cls); }
    function readLessonData(el, key) {
        const directValue = el.dataset[key];
        if (directValue && directValue !== "undefined") return directValue;

        const lessonId = el.dataset.id;
        if (!lessonId) return "";

        const attrName = "data-" + key.replace(/[A-Z]/g, char => "-" + char.toLowerCase());
        const matchingButton = document.querySelector(`.lesson-table-wrapper .form-btn[data-id="${CSS.escape(lessonId)}"][${attrName}]`);
        const matchingValue = matchingButton ? matchingButton.getAttribute(attrName) : "";
        return matchingValue && matchingValue !== "undefined" ? matchingValue : "";
    }

    function emptyPopup(el) {
        const date = el.dataset.date;
        const hour = el.dataset.hour;
        const retDate = date + " " + hour;
        const ids = getIds();
        let dur = 0;
        let cId = 0;
        let stId = stIdInEmptyPopup ? stIdInEmptyPopup.innerText : 0;

        popupDateInEmptyPopup.innerText = date;
        popupTimeInEmptyPopup.innerText = hour;

        if (config.setEmptyForms) {
            config.setEmptyForms(emptyPopupAddLessonForm, emptyPopupAddCourseForm, ids, cId, stId, retDate, dur);
        }

        if (studentIdInEmptyPopup) {
            let stIds = studentIdInEmptyPopup.getElementsByTagName("input");
            for (let i = 0; i < stIds.length; i++) {
                stIds[i].addEventListener('change', () => {
                    if (stIds[i].checked) {
                        stId = stIds[i].value;
                        if (config.setEmptyForms) config.setEmptyForms(emptyPopupAddLessonForm, emptyPopupAddCourseForm, ids, cId, stId, retDate, dur);
                    }
                });
            }
        }

        if (courseIdInEmptyPopup) {
            let cIds = courseIdInEmptyPopup.getElementsByTagName("input");
            for (let i = 0; i < cIds.length; i++) {
                cIds[i].addEventListener('change', () => {
                    if (cIds[i].checked) {
                        cId = cIds[i].value;
                        if (config.setEmptyForms) config.setEmptyForms(emptyPopupAddLessonForm, emptyPopupAddCourseForm, ids, cId, stId, retDate, dur);
                    }
                });
            }
        }

        if (durationInEmptyPopup) {
            durationInEmptyPopup.addEventListener("input", (event) => {
                dur = event.target.value;
                if (config.setEmptyForms) config.setEmptyForms(emptyPopupAddLessonForm, emptyPopupAddCourseForm, ids, cId, stId, retDate, dur);
            });
        }
    }

    function openPastLessonPopup(el) {
        const date = el.dataset.date;
        const hour = el.dataset.hour;
        const stName = readLessonData(el, "sn");
        const cName = readLessonData(el, "cn");
        const status = el.dataset.status;
        const dur = el.dataset.dur;
        const idLes = el.dataset.id;
        const ids = getIds();

        removeClass(pastLessonPopup, "hide");
        if (popup) removeClass(popup, "hide");

        popupDateInPastPopup.innerText = date;
        popupTimeInPastPopup.innerText = hour;
        popupStudentNameInPastPopup.innerText = stName;
        popupCourseNameInPastPopup.innerText = cName;
        popupStatusInPastPopup.innerText = status;
        if (durInMain) durInMain.innerText = dur;
        if (durInEdit) durInEdit.innerText = dur;
        if (durInPast) durInPast.innerText = dur;

        if (cost) {
            cost.innerText = (status === "was") ? el.dataset.cost : 0;
        }

        if (wasFormInPastPopup && config.setWasFormInPastPopup) {
            config.setWasFormInPastPopup(wasFormInPastPopup, ids, idLes, status);
        }
    }

    function openPopup(el) {
        removeClass(emptyPopupEl, "hide");
        if (popupEditBtn) removeClass(popupEditBtn, "hide");
        if (popupDeleteBtn) removeClass(popupDeleteBtn, "hide");
        if (popupDeleteCourseBtn) removeClass(popupDeleteCourseBtn, "hide");

        if (el.dataset.lesson == null) {
            addClass(popupMain, "hide");
            addClass(popupEdit, "hide");
            removeClass(popup, "hide");
            emptyPopup(el);
            return;
        }

        addClass(emptyPopupEl, "hide");
        removeClass(popup, "hide");
        removeClass(popupMain, "hide");
        addClass(popupEdit, "hide");

        const date = el.dataset.date;
        const hour = el.dataset.hour;
        const lesson = el.dataset.lesson;
        const idLes = el.dataset.id;
        const teacherName = readLessonData(el, "tn");
        const studentName = readLessonData(el, "sn");
        const dur = el.dataset.dur;
        const courseName = readLessonData(el, "cn");
        const ids = getIds();

        let teachFreeTimes = [];
        try {
            const raw = el.dataset.tfts;
            if (raw && raw !== "{}") teachFreeTimes = JSON.parse(raw);
        } catch (e) {}

        popupDate.innerText = date;
        popupTime.innerText = hour;
        popupLessonID.innerText = idLes;
        popupCourseName.innerText = courseName;
        if (popupStudentName) popupStudentName.innerText = studentName;
        if (popupTeacherName) popupTeacherName.innerText = teacherName;
        popupTimeInEdit.innerText = hour;
        popupDateInEdit.innerText = date;
        popupCourseNameInEdit.innerText = courseName;
        if (popupStudentNameInEdit) popupStudentNameInEdit.innerText = studentName;
        if (popupTeacherNameInEdit) popupTeacherNameInEdit.innerText = teacherName;
        if (durInMain) durInMain.innerText = dur;
        if (durInEdit) durInEdit.innerText = dur;
        if (durInPast) durInPast.innerText = dur;

        if (lesson == null) {
            if (popupEditBtn) addClass(popupEditBtn, "hide");
            if (popupDeleteBtn) addClass(popupDeleteBtn, "hide");
            if (popupDeleteCourseBtn) addClass(popupDeleteCourseBtn, "hide");
        }

        if (config.setForms) {
            config.setForms(deleteLessonForm, deleteCourseForm, wasForm, ids, idLes);
        }

        if (popupEditBtn) {
            popupEditBtn.addEventListener("click", () => {
                addClass(popupMain, "hide");
                removeClass(popupEdit, "hide");

                if (teachFreeTimes.length === 0) {
                    let lable = document.createElement("lable");
                    lable.style.whiteSpace = "nowrap";
                    lable.innerText = "Нажаль ви не можете перенести урок адже у вчителя немає вільного часу";
                    popupEditDates.appendChild(lable);
                }

                for (let element in teachFreeTimes) {
                    const { display, isoDate, minutes } = formatDateFromKey(element);
                    const dayName = getDayName(teachFreeTimes[element].dayOfTheWeek);
                    console.log(isoDate)

                    let inp = document.createElement("input");
                    inp.type = "checkbox";
                    inp.name = "newTimeIds";
                    inp.value = teachFreeTimes[element].id + " " + isoDate;

                    let labl = document.createElement("lable");
                    labl.innerText = display + " " + dayName + " " + teachFreeTimes[element].timeOfTheDay + ":" + minutes;

                    popupEditDates.appendChild(inp);
                    popupEditDates.appendChild(labl);
                }

                let nTIds = popupEditDates.getElementsByTagName("input");
                let nTId = 0;
                let retDate = "";
                for (let i1 = 0; i1 < nTIds.length; i1++) {
                    nTIds[i1].addEventListener('change', () => {
                        if (nTIds[i1].checked) {
                            nTId = nTIds[i1].value.split(" ")[0];
                            retDate = nTIds[i1].value.split(" ")[1]+" "+nTIds[i1].value.split(" ")[2]+" "+nTIds[i1].value.split(" ")[3];
                            if (config.setEditForm) config.setEditForm(editForm, ids, idLes, nTId, retDate);
                        }
                        returnIdValue.innerText = nTId;
                    });
                }
            }, { once: true });
        }
    }
    editPopup
    function closePopup() {
        addClass(popupEdit, "hide");
        if (popup) addClass(popup, "hide");
        if (pastLessonPopup) addClass(pastLessonPopup, "hide");
        addClass(popupMain, "hide");
        if (emptyPopupEl) addClass(emptyPopupEl, "hide");
        while (popupEditDates.firstChild) {
            popupEditDates.removeChild(popupEditDates.firstChild);
        }
    }

    // Експортуємо функції в глобальний scope для th:onclick
    window.openPopup = openPopup;
    window.openPastLessonPopup = openPastLessonPopup;
    window.closePopup = closePopup;
}


// =============================================================================
// LESSON PERIOD PERSISTENCE
// =============================================================================
function getAlertStatusValues() {
    return new Set([
        'GENERAL', 'general',
        'EXISTS', 'exists',
        'NO_PASS', 'noPass',
        'TOO_LATE',
        'TOO_EARLY',
        'LESSON_BEFORE_NOW',
        'COURSE_NOT_FOUND',
        'STUDENT_NOT_FOUND',
        'LESSON_AFTER_ACCEPTED_TIME', 'lessonAfterAcceptedTime',
        'DATE_INCONSISTENCY', 'dateInconsistency',
        'TIME_NOT_EMPTY', 'timeNotEmpty',
        'ITEM_NOT_PICKED', 'itemNotPicked', 'notPicked',
        'LESSON_OVERLAP', 'lessonOverlap',
        'NO_COURSES',
        'NO_FREE_TIMES',
        'WRONG_PASSWORD',
        'USER_NOT_FOUND',
        'LESSON_STATUS_CHANGED',
        'LESSON_ADDED', 'lessonAdded',
        'COURSE_ADDED', 'courseAdded',
        'LESSON_DELETED', 'lessonDeleted',
        'COURSE_DELETED', 'courseDeleted',
        'LESSON_EDITED', 'lessonEdited',
        'TEACHER_ADDED',
        'STUDENT_ADDED',
        'STUDENT_DELETED',
        'TEACHER_DELETED',
        'STUDENT_EDITED', 'studentEdited',
        'TEACHER_EDITED', 'teacherEdited',
        'MONEY_ADDED',
        'DELETE_ACCEPTED',
        'DELETE_DENYED',
        'notValid'
    ]);
}

function getPathWithoutAlertStatus() {
    const statusValues = getAlertStatusValues();
    const pathParts = window.location.pathname.split('/').filter(Boolean);
    if (pathParts.length === 0) return window.location.pathname;

    const lastPart = decodeURIComponent(pathParts[pathParts.length - 1]);
    if (statusValues.has(lastPart)) {
        pathParts.pop();
        return '/' + pathParts.join('/');
    }

    return window.location.pathname;
}

(function initLessonPeriodPersistence() {
    const lessonDaysSelect = document.querySelector('.lesson-period-form select[name="days"]');
    if (!lessonDaysSelect || !window.localStorage) return;

    const validValues = new Set(['1', '7', '30']);
    const storageKey = 'lessonDays:' + getPathWithoutAlertStatus();
    const params = new URLSearchParams(window.location.search);
    const daysFromUrl = params.get('days');

    if (validValues.has(daysFromUrl)) {
        localStorage.setItem(storageKey, daysFromUrl);
    } else {
        const savedDays = localStorage.getItem(storageKey);
        if (validValues.has(savedDays) && savedDays !== lessonDaysSelect.value) {
            params.set('days', savedDays);
            window.location.replace(window.location.pathname + '?' + params.toString() + window.location.hash);
            return;
        }
    }

    lessonDaysSelect.addEventListener('change', () => {
        if (validValues.has(lessonDaysSelect.value)) {
            localStorage.setItem(storageKey, lessonDaysSelect.value);
        }
    });

    const form = lessonDaysSelect.closest('form');
    if (form) {
        form.addEventListener('submit', () => {
            if (validValues.has(lessonDaysSelect.value)) {
                localStorage.setItem(storageKey, lessonDaysSelect.value);
            }
        });
    }
})();


// =============================================================================
// REMOVE STATUS FROM URL AFTER ALERT IS RENDERED
// =============================================================================
(function clearAlertStatusFromUrl() {
    const alert = document.querySelector('.error-box, .success-box');
    if (!alert || !window.history || !window.history.replaceState) return;

    const cleanPath = getPathWithoutAlertStatus();
    if (cleanPath === window.location.pathname) return;

    const cleanUrl = cleanPath + window.location.search + window.location.hash;
    window.history.replaceState(window.history.state, document.title, cleanUrl);
})();


// =============================================================================
// PAGE LOADING OVERLAY
// =============================================================================
(function initPageLoadingOverlay() {
    function getOverlay() {
        let overlay = document.getElementById('page-loading-overlay');
        if (overlay) return overlay;

        overlay = document.createElement('div');
        overlay.id = 'page-loading-overlay';
        overlay.className = 'page-loading-overlay';
        overlay.setAttribute('aria-live', 'polite');
        overlay.setAttribute('aria-label', 'Сторінка завантажується');
        overlay.innerHTML = '<div class="page-loading-box"><div class="page-loading-spinner"></div><div class="page-loading-text">Завантаження...</div></div>';
        document.body.appendChild(overlay);
        return overlay;
    }

    function showLoading() {
        getOverlay().classList.add('visible');
    }

    document.addEventListener('submit', (event) => {
        if (!event.defaultPrevented) {
            showLoading();
        }
    }, true);

    document.addEventListener('click', (event) => {
        const link = event.target.closest('a[href]');
        if (!link || event.defaultPrevented) return;
        if (event.ctrlKey || event.metaKey || event.shiftKey || event.altKey) return;
        if (link.target && link.target !== '_self') return;

        const href = link.getAttribute('href');
        if (!href || href.startsWith('#') || href.startsWith('javascript:') || href.startsWith('mailto:') || href.startsWith('tel:')) return;

        showLoading();
    }, true);

    window.addEventListener('pageshow', () => {
        const overlay = document.getElementById('page-loading-overlay');
        if (overlay) overlay.classList.remove('visible');
    });
})();


// =============================================================================
// BACK TO TOP BUTTON
// =============================================================================
// Ініціалізатор підключає обробники подій після завантаження DOM-елементів.
// Такий підхід дозволяє одному JS-файлу працювати на різних сторінках, де частина елементів може бути відсутня.
(function initBackToTop() {
    window.addEventListener('scroll', () => {
        const backToTopBtn = document.getElementById('back-to-top');
        if (!backToTopBtn) return;
        
        if (window.scrollY > 400) {
            backToTopBtn.classList.add('visible');
        } else {
            backToTopBtn.classList.remove('visible');
        }
    });

    document.addEventListener('click', (e) => {
        const btn = e.target.closest('#back-to-top');
        if (btn) {
            e.preventDefault();
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        }
    });
})();
