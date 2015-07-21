# install golang
cd /tmp && wget https://storage.googleapis.com/golang/go1.4.linux-amd64.tar.gz
cd /tmp && sudo tar -C /usr/local -xzf go1.4.linux-amd64.tar.gz

# export
export GOPATH=/vagrant/combiner/workspace
export PATH=${PATH}:/usr/local/go/bin:${GOPATH}/bin

# build
cd $GOPATH/bin && go install main && mv main combiner

echo "PATH=\${PATH}:${GOPATH}/bin" >> ~/.profile
