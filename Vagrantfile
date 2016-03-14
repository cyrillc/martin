Vagrant.configure(2) do |config|
	config.vm.box = "martin14a/devbox"
  	config.vm.network :forwarded_port, guest: 4848, host: 4848
  	config.vm.network :forwarded_port, guest: 4141, host: 4141
  	config.vm.network :forwarded_port, guest: 4040, host: 4040
  	config.vm.network :forwarded_port, guest: 8080, host: 8080
        config.vm.provision :shell, path: "startup.sh"
end
