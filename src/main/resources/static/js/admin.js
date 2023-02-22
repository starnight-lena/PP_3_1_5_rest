const dbRoles = [{id: 1, role: "ROLE_ADMIN"}, {id: 2, role: "ROLE_USER"}]
const token = document.querySelector("meta[name='_csrf']").content
//all users
const adminurl = '/api/admin';

async function getAdminPage() {
    let page = await fetch(adminurl);

    if (page.ok) {
        let listAllUser = await page.json();
        loadTableData(listAllUser);
    } else {
        alert(`Error, ${page.status}`)
    }
}

function loadTableData(listAllUser) {
    const tableBody = document.getElementById('all-users');
    let dataHtml = '';
    for (let user of listAllUser) {
        let roles = [];
        for (let role of user.roles) {
            roles.push(" " + role.name.toString()
                .replaceAll("ROLE_", ""))
        }
        dataHtml +=
            `<tr>
    <td>${user.id}</td>
    <td>${user.username}</td>
    <td>${user.firstName}</td>
    <td>${user.lastName}</td>
    <td>${user.age}</td>
    <td>${user.email}</td>
    <td>${roles}</td>
    <td>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#editModal"
        onclick="loadDataForEditModal(${user.id})">Edit</button>
    </td>
        
    <td>
        <button type="button" class="btn btn-danger deleteBtn" data-toggle="modal" data-target="#delteModal"
        onclick="deleteModalData(${user.id})">Delete</button>
    </td>
   
</tr>`
    }
    tableBody.innerHTML = dataHtml;
}

getAdminPage();

//Delete modal

const delId = document.getElementById('userIdDelete');
const delUsername = document.getElementById('usernameDelete');
const delFirstName = document.getElementById('firstNameDelete');
const delLastName = document.getElementById('lastNameDelete');
const delAge = document.getElementById('ageDelete');
const delEmail = document.getElementById('emailDelete');
const delRole = document.getElementById("rolesDelete")
const deleteModal = document.getElementById("deleteModal");
const closeDeleteButton = document.getElementById("closeDelete")
const bsDeleteModal = new bootstrap.Modal(deleteModal);

async function deleteModalData(id) {
    const urlForDel = 'api/admin/' + id;
    let usersPageDel = await fetch(urlForDel);
    if (usersPageDel.ok) {
        let userData =
            await usersPageDel.json().then(user => {
                delId.value = `${user.id}`;
                delUsername.value = `${user.username}`;
                delFirstName.value = `${user.firstName}`;
                delLastName.value = `${user.lastName}`;
                delAge.value = `${user.age}`;
                delEmail.value = `${user.email}`;
                delRole.innerHTML = `
                    <option value="${dbRoles[0].id}">${dbRoles[0].role}</option>
                    <option value="${dbRoles[1].id}">${dbRoles[1].role}</option>
                   `
                Array.from(delRole.options).forEach(opt => {
                    user.roles.forEach(role => {
                        if (role.name === opt.text) {
                            opt.selected = true
                        }
                    })
                })
            })

        bsDeleteModal.show();
    } else {
        alert(`Error, ${usersPageDel.status}`)
    }
}

async function deleteUser() {
    let urlDel = 'api/admin/' + delId.value;
    let method = {
        method: 'DELETE', // Method itself
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
            'X-CSRF-TOKEN': token
        },
    }

    fetch(urlDel, method).then(() => {
        closeDeleteButton.click();
        getAdminPage();
    })
}

const editForm = document.getElementById('editForm');
const editId = document.getElementById('idEdit');
const editUsername = document.getElementById('usernameEdit');
const editFirstName = document.getElementById('firstNameEdit');
const editLastName = document.getElementById('lastNameEdit');
const editAge = document.getElementById('ageEdit');
const editEmail = document.getElementById('emailEdit');
const editRole = document.getElementById("rolesEdit")
const editModal = document.getElementById("editModal");
const closeEditButton = document.getElementById("editClose")
const bsEditModal = new bootstrap.Modal(editModal);

async function loadDataForEditModal(id) {
    const urlDataEd = 'api/admin/' + id;
    let usersPageEd = await fetch(urlDataEd);
    if (usersPageEd.ok) {
        await usersPageEd.json().then(user => {
            console.log('userData', JSON.stringify(user))
            editId.value = `${user.id}`;
            editUsername.value = `${user.username}`;
            editFirstName.value = `${user.firstName}`;
            editLastName.value = `${user.lastName}`;
            editAge.value = `${user.age}`;
            editEmail.value = `${user.email}`;
            editRole.innerHTML = `
                    <option value="${dbRoles[0].id}">${dbRoles[0].role}</option>
                    <option value="${dbRoles[1].id}">${dbRoles[1].role}</option>
                   `
            Array.from(editRole.options).forEach(opt => {
                user.roles.forEach(role => {
                    if (role.name === opt.text) {
                        opt.selected = true
                    }
                })
            })
        })
        bsEditModal.show();
    } else {
        alert(`Error, ${usersPageEd.status}`)
    }
}

async function editUser() {
    console.log('sss');
    let urlEdit = 'api/admin/';
    let listOfRole = [];
    console.dir(editForm)
    for (let i = 0; i < editForm.roles.options.length; i++) {
        if (editForm.roles.options[i].selected) {
            let tmp = {};
            tmp["id"] = editForm.roles.options[i].value
            listOfRole.push(tmp);
        }
    }
    let method = {
        method: 'PATCH',
        headers: {
            "Content-Type": "application/json",
            'X-CSRF-TOKEN': token
        },
        body: JSON.stringify({
            id: editId.value,
            username: editForm.username.value,
            firstName: editForm.firstName.value,
            lastName: editForm.lastName.value,
            age: editForm.age.value,
            email: editForm.email.value,
            password: editForm.password.value,
            roles: listOfRole
        })
    }
    console.log(urlEdit, method)
    await fetch(urlEdit, method).then(() => {
        closeEditButton.click();
        getAdminPage();
    })
}

const newUserForm = document.getElementById('createUserForm');
const newRoles = document.getElementById('roles');

newRoles.innerHTML = `
                    <option value="${dbRoles[0].id}">${dbRoles[0].role}</option>
                    <option value="${dbRoles[1].id}">${dbRoles[1].role}</option>
                   `

newUserForm.addEventListener('submit', addNewUser);

async function addNewUser(event) {
    event.preventDefault();
    const urlNew = 'api/admin';
    const newRole = document.querySelector('#roles').selectedOptions;
    let listOfRole = [];
    for (let i = 0; i < newRole.length; i++) {
        listOfRole.push({
            id: newRole[i].value
        });
    }
    let method = {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
            'X-CSRF-TOKEN': token
        },
        body: JSON.stringify({
            username: newUserForm.username.value,
            firstName: newUserForm.firstName.value,
            lastName: newUserForm.lastName.value,
            age: newUserForm.age.value,
            email: newUserForm.email.value,
            password: newUserForm.password.value,
            roles: listOfRole
        })
    }
    await fetch(urlNew, method).then(() => {
        newUserForm.reset();
        $('[href="#usersTable"]').tab('show');
        getAdminPage();

    });

}

