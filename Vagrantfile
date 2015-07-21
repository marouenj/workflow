Vagrant.configure("2") do |config|

	config.vm.define "workflow" do |workflow|
		workflow.vm.box = "ubuntu/trusty64"
		workflow.vm.hostname = "workflow"
		workflow.vm.provision :shell, :path => "vagrant-init/bootstrap.common.sh"
		workflow.vm.provision :shell, :path => "vagrant-init/bootstrap.go.sh"
		workflow.vm.provision :shell, :path => "vagrant-init/bootstrap.python.sh"
		workflow.vm.provision :shell, :path => "vagrant-init/bootstrap.java.sh"

		workflow.vm.provider "virtualbox" do |vbox|
			vbox.name = "workflow"
			vbox.memory = 512
		end
	end

end
