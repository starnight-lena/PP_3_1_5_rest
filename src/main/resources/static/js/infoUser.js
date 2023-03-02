const userurl = '/api/user';


const infoUser = fetch(userurl).then(response => response.json())
infoUser.then(user => {
        let roles = ''
        user.roles.forEach(role => {
            roles += ' '
            roles += role.name
        })
        console.log(roles)
        document.getElementById("navbar-email").innerHTML = user.email
        document.getElementById("navbar-roles").innerHTML = roles
    }
)

async function getUserPage() {
    let page = await fetch(userurl);

    if (page.ok) {
        let user = await page.json();
        getInformationAboutUser(user);
    } else {
        alert(`Error, ${page.status}`)
    }
}

function getInformationAboutUser(user) {
    const tableBody = document.getElementById('tbody-user');
    let dataHtml = '';
    let roles = [];
    console.log('userSata', JSON.stringify(user))
    for (let role of user.roles) {
        roles
        roles.push(" " + role.name.toString()
            .replaceAll("ROLE_", ""))
    }
    dataHtml =
        `<tr>
    <td>${user.id}</td>
    <td>${user.username}</td>
    <td>${user.firstName}</td>
    <td>${user.lastName}</td>
    <td>${user.age}</td>
    <td>${user.email}</td>
    <td>${roles}</td>   
</tr>`

    tableBody.innerHTML = dataHtml;
}

getUserPage();