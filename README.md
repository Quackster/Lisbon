# Lisbon

Lisbon is a Habbo Hotel emulator that is designed to fully emulate the v26 version from 2008 era. 

All the gamedata files can be found [HERE](https://h4bbo.net/archive/dcr_packs/dcr_complete_r26_20080915_0408_7984_61ccb5f8b8797a3aba62c1fa2ca80169.zip).

# Screenshots

Website:

<img width="794" height="622" alt="image" src="https://github.com/user-attachments/assets/02a221bf-5f08-4fe9-a68d-5f7da6262ab1" />

Logged in:

<img width="1438" height="1018" alt="basilisk_SN5B5EZHaQ" src="https://github.com/user-attachments/assets/ca5cc487-08ea-4b62-9627-4b2d9c4c3f71" />

Hotel:

<img width="960" height="540" alt="basilisk_h44Zb4dNb6" src="https://github.com/user-attachments/assets/daa685f6-5891-4f88-8783-34d6b135ebfb" />

## Linux install

The Linux installer deploys Lisbon with Java 17, MariaDB, Nginx, the emulator service, the web service, database imports, and systemd units.

Run it on a systemd-based Linux server:

```bash
sudo ./install-linux.sh --host example.com
```

Useful options:

```bash
sudo ./install-linux.sh \
  --host example.com \
  --db-name lisbon \
  --db-user lisbon \
  --game-port 30000 \
  --mus-port 12322 \
  --web-backend-port 8080
```

After installation:

```bash
systemctl status lisbon-server lisbon-web nginx mariadb --no-pager
journalctl -u lisbon-server -f
journalctl -u lisbon-web -f
```

## Quick VM test

The quickest realistic test is a disposable Ubuntu VM with Multipass.

From Windows PowerShell:

```powershell
multipass launch 24.04 --name lisbon-test --cpus 2 --memory 4G --disk 20G
multipass mount C:\SourceControl\Lisbon lisbon-test:/src/Lisbon
multipass shell lisbon-test
```

Inside the VM:

```bash
cd /src/Lisbon
chmod +x install-linux.sh
sudo ./install-linux.sh --host "$(hostname -I | awk '{print $1}')"
```

Check the services:

```bash
systemctl status lisbon-server lisbon-web nginx mariadb --no-pager
curl -I http://localhost/
journalctl -u lisbon-server -n 100 --no-pager
journalctl -u lisbon-web -n 100 --no-pager
```

From Windows, get the VM IP:

```powershell
multipass info lisbon-test
```

Open `http://<vm-ip>/` in a browser.

Clean up the VM when finished:

```powershell
multipass delete lisbon-test
multipass purge
```
