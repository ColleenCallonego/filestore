<aside class="aside-container">
    <div class="aside-inner">
        <nav class="sidebar" data-sidebar-anyclick-close="">
            <ul class="sidebar-nav">
                <li class="has-user-block">
                    <div class="collapse show" id="user-block">
                        <div class="item user-block">
                            <div class="user-block-picture">
                                <div class="user-block-status">
                                    <img class="img-thumbnail rounded-circle" src="https://www.gravatar.com/avatar/${content.profile.getGravatarHash()}" alt="Avatar" width="60" height="60">
                                    <div class="circle bg-success circle-lg"></div>
                                </div>
                            </div>
                            <div class="user-block-info">
                                <span class="user-block-name">Hello, ${content.profile.fullname}</span>
                                <span class="user-block-role">${content.profile.email}</span>
                            </div>
                        </div>
                    </div>
                </li>
                <li class="${(section == 'files')?then('active','')}">
                    <a href="/api/files" title="Mon Espace">
                        <em class="far fa-hdd"></em>
                        <span data-localize="sidebar.nav.SINGLEVIEW">Mon Espace</span>
                    </a>
                </li>
                <li class="${(section == 'shares')?then('active','')}">
                    <a href="/api/shares" title="Mes Partages">
                        <em class="far fa-handshake"></em>
                        <span data-localize="sidebar.nav.SINGLEVIEW">Mes Partages</span>
                    </a>
                </li>
                <li class="${(section == 'network')?then('active','')}">
                    <a href="/api/network" title="Mes Voisins">
                        <em class="icon-globe"></em>
                        <span data-localize="sidebar.nav.SINGLEVIEW">Mes Voisins</span>
                    </a>
                </li>
                <li class="${(section == 'status')?then('active','')}">
                    <a href="/api/status" title="Status">
                        <em class="icon-speedometer"></em>
                        <span data-localize="sidebar.nav.SINGLEVIEW">Status</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</aside>